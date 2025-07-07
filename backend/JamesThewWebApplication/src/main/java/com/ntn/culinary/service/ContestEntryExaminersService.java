package com.ntn.culinary.service;

import com.ntn.culinary.constant.ContestEntryStatusType;
import com.ntn.culinary.dao.ContestEntryDao;
import com.ntn.culinary.dao.ContestEntryExaminersDao;
import com.ntn.culinary.dao.UserDao;
import com.ntn.culinary.model.ContestEntryExaminers;
import com.ntn.culinary.request.ContestEntryExaminersRequest;

import java.sql.Timestamp;

public class
ContestEntryExaminersService {

    private final ContestEntryDao contestEntryDao;
    private final ContestEntryExaminersDao contestEntryExaminersDao;
    private final UserDao userDao;

    public ContestEntryExaminersService(ContestEntryDao contestEntryDao, ContestEntryExaminersDao contestEntryExaminersDao, UserDao userDao) {
        this.contestEntryDao = contestEntryDao;
        this.contestEntryExaminersDao = contestEntryExaminersDao;
        this.userDao = userDao;
    }

    public void addExaminer(ContestEntryExaminersRequest request) {
        try {
            // Validate the request before proceeding
            validateRequest(request);

            // Add the examiner to the contest entry
            contestEntryExaminersDao.addContestEntryExaminer(mapRequestToModel(request));

            // Update the contest entry status to REVIEWED
            contestEntryDao.updateContestEntryStatus(request.getContestEntryId(), String.valueOf(ContestEntryStatusType.REVIEWED));
        } catch (Exception e) {
            // Log the error or handle it as needed
            throw new RuntimeException("Error adding examiner: " + e.getMessage(), e);
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

    private void validateRequest(ContestEntryExaminersRequest request) {
        if (!contestEntryDao.existsById(request.getContestEntryId())) {
            throw new RuntimeException("Contest entry does not exist");
        }

        if (!userDao.existsById(request.getExaminerId())) {
            throw new RuntimeException("Examiner does not exist");
        }

        if (contestEntryExaminersDao.existsByContestEntryIdAndExaminerId(request.getContestEntryId(), request.getExaminerId())) {
            throw new RuntimeException("Examiner has already reviewed this contest entry");
        }
    }
}
