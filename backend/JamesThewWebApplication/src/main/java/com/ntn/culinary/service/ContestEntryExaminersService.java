package com.ntn.culinary.service;

import com.ntn.culinary.constant.ContestEntryStatusType;
import com.ntn.culinary.dao.ContestEntryDAO;
import com.ntn.culinary.dao.ContestEntryExaminersDAO;
import com.ntn.culinary.dao.UserDAO;
import com.ntn.culinary.model.ContestEntryExaminers;
import com.ntn.culinary.request.ContestEntryExaminersRequest;

import java.sql.Date;
import java.sql.Timestamp;

public class ContestEntryExaminersService {
    private static final ContestEntryExaminersService contestEntryExaminersService = new ContestEntryExaminersService();

    private ContestEntryExaminersService() {
        // Private constructor to prevent instantiation
    }

    public static ContestEntryExaminersService getInstance() {
        return contestEntryExaminersService;
    }

    private final ContestEntryExaminersDAO contestEntryExaminersDAO = ContestEntryExaminersDAO.getInstance();
    private final ContestEntryDAO contestEntryDAO = ContestEntryDAO.getInstance();
    private final UserDAO userDAO = UserDAO.getInstance();

    public void addExaminer(ContestEntryExaminersRequest request) throws Exception {
        try {
            // Validate the request before proceeding
            validateRequest(request);

            // Add the examiner to the contest entry
            contestEntryExaminersDAO.addContestEntryExaminer(mapRequestToModel(request));

            // Update the contest entry status to REVIEWED
            contestEntryDAO.updateContestEntryStatus(request.getContestEntryId(), String.valueOf(ContestEntryStatusType.REVIEWED));
        } catch (Exception e) {
            // Log the error or handle it as needed
            throw new Exception("Error adding examiner: " + e.getMessage(), e);
        }
    }

    private ContestEntryExaminers mapRequestToModel(ContestEntryExaminersRequest request) {
        ContestEntryExaminers examiner = new ContestEntryExaminers();
        examiner.setContestEntryId(request.getContestEntryId());
        examiner.setExaminerId(request.getExaminerId());
        examiner.setScore(request.getScore());
        examiner.setFeedback(request.getFeedback());
        examiner.setExamDate(new Timestamp(System.currentTimeMillis()));
        return examiner;
    }

    private void validateRequest(ContestEntryExaminersRequest request) throws Exception {
        if (!contestEntryDAO.existsById(request.getContestEntryId())) {
            throw new Exception("Contest entry does not exist");
        }

        if (!userDAO.existsById(request.getExaminerId())) {
            throw new Exception("Examiner does not exist");
        }

        if (contestEntryExaminersDAO.existsByContestEntryIdAndExaminerId(request.getContestEntryId(), request.getExaminerId())) {
            throw new Exception("Examiner has already reviewed this contest entry");
        }
    }
}
