package com.ntn.culinary.service;

import com.ntn.culinary.dao.*;
import com.ntn.culinary.exception.ConflictException;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.model.ContestEntry;
import com.ntn.culinary.model.ContestEntryInstruction;
import com.ntn.culinary.request.ContestEntryRequest;
import com.ntn.culinary.utils.ImageUtils;

import javax.servlet.http.Part;
import java.sql.Date;

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
                            contestEntryId,
                            instructions.getStepNumber(),
                            instructions.getName(),
                            instructions.getText(),
                            instructions.getImage()
                    )
            );
        }
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
}
