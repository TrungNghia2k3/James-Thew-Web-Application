package com.ntn.culinary.service;

import com.ntn.culinary.dao.*;
import com.ntn.culinary.model.ContestEntry;
import com.ntn.culinary.model.ContestEntryInstruction;
import com.ntn.culinary.request.ContestEntryRequest;
import com.ntn.culinary.utils.ImageUtils;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.Part;
import java.sql.Date;

public class ContestEntryService {
    private static final ContestEntryService contestEntryService = new ContestEntryService();

    private ContestEntryService() {
        // Private constructor to prevent instantiation
    }

    public static ContestEntryService getInstance() {
        return contestEntryService;
    }

    private final ContestEntryDAO contestEntryDAO = ContestEntryDAO.getInstance();
    private final ContestEntryInstructionsDAO contestEntryInstructionsDAO = ContestEntryInstructionsDAO.getInstance();
    private final UserDAO userDAO = UserDAO.getInstance();
    private final ContestDAO contestDAO = ContestDAO.getInstance();
    private final CategoryDAO categoryDAO = CategoryDAO.getInstance();
    private final AreaDAO areaDAO = AreaDAO.getInstance();

    public void addContestEntry(ContestEntryRequest contestEntryRequest, Part imagePart) throws Exception {

        // Validate the contest entry request
        validateContestEntryRequest(contestEntryRequest);

        if (imagePart != null && imagePart.getSize() > 0) {
            String slug = ImageUtils.slugify(contestEntryRequest.getName());
            String fileName = ImageUtils.saveImage(imagePart, slug, "contest_entries");
            contestEntryRequest.setImage(fileName);
        }

        // insert contest entry
        contestEntryDAO.addContestEntry(mapRequestToContestEntry(contestEntryRequest));

        // get the contest entry ID
        int contestEntryId = contestEntryDAO.getContestEntryIdByUserIdAndContestId(contestEntryRequest.getUserId(), contestEntryRequest.getContestId());

        // insert contest entry instructions
        for (ContestEntryInstruction instructions : mapRequestToContestEntry(contestEntryRequest).getContestEntryInstructions()) {

            contestEntryInstructionsDAO.addContestEntryInstructions(
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

    private void validateContestEntryRequest(@NotNull ContestEntryRequest request) throws Exception {

        if (!userDAO.existsById(request.getUserId())) {
            throw new Exception("User with ID " + request.getUserId() + " does not exist.");
        }

        if (!contestDAO.existsById(request.getContestId())) {
            throw new Exception("Contest with ID " + request.getContestId() + " does not exist.");
        }

        if (contestEntryDAO.existsByUserIdAndContestIdAndName(request.getUserId(), request.getContestId(), request.getName()))
            throw new Exception("Contest entry with the same name already exists for this user and contest.");

        if (!categoryDAO.existsByName(request.getCategory())) {
            throw new Exception("Category " + request.getCategory() + " does not exist.");
        }

        if (!areaDAO.existsByName(request.getArea())) {
            throw new Exception("Area " + request.getArea() + " does not exist.");
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
}
