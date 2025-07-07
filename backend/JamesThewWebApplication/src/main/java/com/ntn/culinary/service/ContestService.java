package com.ntn.culinary.service;

import com.ntn.culinary.dao.ContestDao;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.model.Contest;
import com.ntn.culinary.model.ContestImages;
import com.ntn.culinary.response.ContestResponse;

import java.util.List;

public class ContestService {
    private final ContestDao contestDao;

    public ContestService(ContestDao contestDao) {
        this.contestDao = contestDao;
    }

    public List<ContestResponse> getAllContests() {
        return contestDao.getAllContests().stream()
                .map(this::mapContestToResponse)
                .toList();
    }

    public ContestResponse getContestById(int id) {
        Contest contest = contestDao.getContestById(id);
        if (contest == null) {
            throw new NotFoundException("Contest not found");
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
