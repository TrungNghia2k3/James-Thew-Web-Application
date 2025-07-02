package com.ntn.culinary.service;

import com.ntn.culinary.dao.ContestDAO;
import com.ntn.culinary.model.Contest;
import com.ntn.culinary.model.ContestImages;
import com.ntn.culinary.response.ContestResponse;

import java.sql.SQLException;
import java.util.List;

public class ContestService {
    private static final ContestService contestService = new ContestService();

    private ContestService() {
        // Private constructor to prevent instantiation
    }

    public static ContestService getInstance() {
        return contestService;
    }

    private final ContestDAO contestDAO = ContestDAO.getInstance();

    public List<ContestResponse> getAllContests() throws SQLException {
        return contestDAO.getAllContests().stream()
                .map(this::mapContestToResponse)
                .toList();
    }

    public ContestResponse getContestById(int id) throws SQLException {
        Contest contest = contestDAO.getContestById(id);
        if (contest == null) {
            return null;
        }
        return mapContestToResponse(contest);
    }

    private ContestResponse mapContestToResponse(Contest contest) {
        String url = "http://localhost:8080/JamesThewWebApplication/api/images/contests/";

        List<ContestImages> images = contest.getContestImages()
                .stream()
                .peek(image -> image.setImagePath(url + image.getImagePath()))
                .toList();

        return new ContestResponse(
                contest.getId(),
                contest.getArticleBody(),
                contest.getHeadline(),
                contest.getDescription(),
                contest.getDatePublished(),
                contest.getDateModified(),
                images,
                contest.getAccessRole()
        );
    }
}
