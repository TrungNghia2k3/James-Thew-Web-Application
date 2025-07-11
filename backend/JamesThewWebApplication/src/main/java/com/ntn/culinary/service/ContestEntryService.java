package com.ntn.culinary.service;

import com.ntn.culinary.dao.*;
import com.ntn.culinary.exception.ConflictException;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.model.ContestEntry;
import com.ntn.culinary.model.ContestEntryInstruction;
import com.ntn.culinary.request.ContestEntryRequest;
import com.ntn.culinary.request.DeleteContestEntryRequest;
import com.ntn.culinary.response.ContestEntryResponse;
import com.ntn.culinary.utils.ImageUtils;

import javax.servlet.http.Part;
import java.sql.Date;
import java.util.List;

import static com.ntn.culinary.utils.ImageUtils.saveImage;
import static com.ntn.culinary.utils.ImageUtils.slugify;

public class ContestEntryService {
    private final ContestEntryDao contestEntryDao;
    private final ContestEntryInstructionsDao contestEntryInstructionsDao;
    private final UserDao userDao;
    private final CategoryDao categoryDao;
    private final AreaDao areaDao;
    private final ContestDao contestDao;

    public ContestEntryService(ContestEntryDao contestEntryDao, ContestEntryInstructionsDao contestEntryInstructionsDao, UserDao userDao, CategoryDao categoryDao, AreaDao areaDao, ContestDao contestDao) {
        this.contestEntryDao = contestEntryDao;
        this.contestEntryInstructionsDao = contestEntryInstructionsDao;
        this.userDao = userDao;
        this.categoryDao = categoryDao;
        this.areaDao = areaDao;
        this.contestDao = contestDao;
    }

    public void addContestEntry(ContestEntryRequest contestEntryRequest, Part imagePart) {

        // Validate the contest entry request
        validateContestEntryRequest(contestEntryRequest);

        if (imagePart != null && imagePart.getSize() > 0) {
            String slug = slugify(contestEntryRequest.getName());
            String fileName = saveImage(imagePart, slug, "contest_entries");
            contestEntryRequest.setImage(fileName);
        }

        // Map the request to a ContestEntry model
        ContestEntry contestEntry = mapRequestToContestEntry(contestEntryRequest);

        // insert contest entry
        contestEntryDao.addContestEntry(contestEntry);

        // get the contest entry ID
        int contestEntryId = contestEntryDao.getContestEntryIdByUserIdAndContestId(contestEntryRequest.getUserId(), contestEntryRequest.getContestId());

        // insert contest entry instructions
        for (ContestEntryInstruction instructions : contestEntry.getContestEntryInstructions()) {

            contestEntryInstructionsDao.addContestEntryInstructions(
                    new ContestEntryInstruction(
                            instructions.getId(),
                            contestEntryId,
                            instructions.getStepNumber(),
                            instructions.getName(),
                            instructions.getText(),
                            instructions.getImage()
                    )
            );
        }
    }

    public void updateContestEntry(ContestEntryRequest contestEntryRequest, Part imagePart) {
        if (!contestEntryDao.existsById(contestEntryRequest.getContestId())) {
            throw new NotFoundException("Contest entry with the specified ID does not exist.");
        }

        // Validate the contest entry request
        validateContestEntryRequest(contestEntryRequest);

        if (imagePart != null && imagePart.getSize() > 0) {
            String slug = slugify(contestEntryRequest.getName());
            String fileName = saveImage(imagePart, slug, "contest_entries");
            contestEntryRequest.setImage(fileName);
        }

        // Map the request to a ContestEntry model
        ContestEntry contestEntry = mapRequestToContestEntry(contestEntryRequest);

        // Update the contest entry
        contestEntryDao.updateContestEntry(contestEntry);

        // Update instructions
        for (ContestEntryInstruction instruction : contestEntry.getContestEntryInstructions()) {
            if (instruction.getId() == 0) {
                contestEntryInstructionsDao.addContestEntryInstructions(instruction);
            } else {
                contestEntryInstructionsDao.updateContestEntryInstructions(instruction);
            }
        }
    }

    public void deleteContestEntry(DeleteContestEntryRequest request) {
        if (!contestEntryDao.existsByUserIdAndContestIdAndName(request.getUserId(), request.getContestId(), request.getName())) {
            throw new NotFoundException("Contest entry with the specified user ID, contest ID, and name does not exist.");
        }

        // Delete contest entry instructions
        List<ContestEntryInstruction> instructions = contestEntryInstructionsDao.getContestEntryInstructionsByContestEntryId(request.getContestId());
        for (ContestEntryInstruction instruction : instructions) {
            if (contestEntryInstructionsDao.existsByContestEntryIdAndInstructionId(instruction.getContestEntryId(), instruction.getId())) {
                contestEntryInstructionsDao.deleteContestEntryInstructionsByContestEntryIdAndInstructionId(instruction.getContestEntryId(), instruction.getId());
            }
        }

        // Delete the contest entry
        contestEntryDao.deleteContestEntryByUserIdAndContestIdAndName(request.getUserId(), request.getContestId(), request.getName());
    }

    public ContestEntryResponse getContestEntryByUserIdAndContestId(int userId, int contestId) {
        ContestEntry contestEntry = contestEntryDao.getContestEntryByUserIdAndContestId(userId, contestId);
        if (contestEntry == null) {
            throw new NotFoundException("Contest entry not found for the specified user ID and contest ID.");
        }
        return mapContestEntryToResponse(contestEntry);
    }

    public ContestEntryResponse getContestEntryById(int id) {
        ContestEntry contestEntry = contestEntryDao.getContestEntryById(id);
        if (contestEntry == null) {
            throw new NotFoundException("Contest entry with the specified ID does not exist.");
        }
        return mapContestEntryToResponse(contestEntry);
    }

    public List<ContestEntryResponse> getContestEntriesByContestId(int contestId) {
        List<ContestEntry> contestEntries = contestEntryDao.getContestEntryByContestId(contestId);
        if (contestEntries.isEmpty()) {
            throw new NotFoundException("No contest entries found for the specified contest ID.");
        }
        return contestEntries.stream().map(this::mapContestEntryToResponse).toList();
    }

    public List<ContestEntryResponse> getContestEntriesByUserId(int userId) {
        List<ContestEntry> contestEntries = contestEntryDao.getContestEntriesByUserId(userId);
        if (contestEntries.isEmpty()) {
            throw new NotFoundException("No contest entries found for the specified user ID.");
        }
        return contestEntries.stream().map(this::mapContestEntryToResponse).toList();
    }

    private void validateContestEntryRequest(ContestEntryRequest request) {

        if (!userDao.existsById(request.getUserId())) {
            throw new NotFoundException("User with ID does not exist.");
        }

        if (!contestDao.existsById(request.getContestId())) {
            throw new NotFoundException("Contest with ID does not exist.");
        }

        if (!categoryDao.existsByName(request.getCategory())) {
            throw new NotFoundException("Category does not exist.");
        }

        if (!areaDao.existsByName(request.getArea())) {
            throw new NotFoundException("Area does not exist.");
        }

        if (contestEntryDao.existsByUserIdAndContestIdAndName(request.getUserId(), request.getContestId(), request.getName()))
            throw new ConflictException("Contest entry with the same name already exists for this user and contest.");

        if (contestDao.isContestClosed(request.getContestId())) {
            throw new ConflictException("Cannot update contest entry as the contest is closed.");
        }
    }

    private ContestEntry mapRequestToContestEntry(ContestEntryRequest request) {
        ContestEntry contestEntry = new ContestEntry();
        contestEntry.setContestId(request.getContestId());
        contestEntry.setUserId(request.getUserId());
        contestEntry.setName(request.getName());
        contestEntry.setIngredients(request.getIngredients());
        contestEntry.setInstructions(request.getInstructions());
        contestEntry.setImage(request.getImage());
        contestEntry.setPrepareTime(request.getPrepareTime());
        contestEntry.setCookingTime(request.getCookingTime());
        contestEntry.setYield(request.getYield());
        contestEntry.setCategory(request.getCategory());
        contestEntry.setArea(request.getArea());
        contestEntry.setShortDescription(request.getShortDescription());
        contestEntry.setDateCreated(new Date(System.currentTimeMillis()));
        contestEntry.setDateModified(new Date(System.currentTimeMillis()));
        contestEntry.setStatus("PENDING");
        contestEntry.setContestEntryInstructions(request.getContestEntryInstructions());

        return contestEntry;
    }

    private ContestEntryResponse mapContestEntryToResponse(ContestEntry contestEntry) {
        ContestEntryResponse response = new ContestEntryResponse();
        response.setId(contestEntry.getId());
        response.setContestId(contestEntry.getContestId());
        response.setUserId(contestEntry.getUserId());
        response.setName(contestEntry.getName());
        response.setIngredients(contestEntry.getIngredients());
        response.setInstructions(contestEntry.getInstructions());
        response.setImage(contestEntry.getImage());
        response.setPrepareTime(contestEntry.getPrepareTime());
        response.setCookingTime(contestEntry.getCookingTime());
        response.setYield(contestEntry.getYield());
        response.setCategory(contestEntry.getCategory());
        response.setArea(contestEntry.getArea());
        response.setShortDescription(contestEntry.getShortDescription());
        response.setDateCreated(contestEntry.getDateCreated());
        response.setDateModified(contestEntry.getDateModified());
        response.setStatus(contestEntry.getStatus());
        response.setContestEntryInstructions(contestEntryInstructionsDao.getContestEntryInstructionsByContestEntryId(contestEntry.getId()));

        return response;
    }
}
