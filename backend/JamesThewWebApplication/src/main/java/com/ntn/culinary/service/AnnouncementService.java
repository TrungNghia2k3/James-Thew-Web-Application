package com.ntn.culinary.service;

import com.ntn.culinary.dao.AnnounceWinnerDAO;
import com.ntn.culinary.dao.AnnouncementDAO;
import com.ntn.culinary.dao.ContestDAO;
import com.ntn.culinary.model.AnnounceWinner;
import com.ntn.culinary.model.Announcement;
import com.ntn.culinary.request.AnnouncementRequest;

import java.sql.Date;

public class AnnouncementService {
    private static final AnnouncementService announcementService = new AnnouncementService();

    private AnnouncementService() {
        // Private constructor to prevent instantiation
    }

    public static AnnouncementService getInstance() {
        return announcementService;
    }

    private final ContestDAO contestDAO = ContestDAO.getInstance();
    private final AnnouncementDAO announcementDAO = AnnouncementDAO.getInstance();
    private final AnnounceWinnerDAO announceWinnerDAO = AnnounceWinnerDAO.getInstance();

    public void addAnnouncement(AnnouncementRequest announcementRequest) throws Exception {
        // Validate the announcement request
        validateAnnouncementRequest(announcementRequest);

        // Insert the announcement into the database
        announcementDAO.insertAnnouncement(mapRequestToAnnouncement(announcementRequest));

        // get the announcement ID
        int announcementId = announcementDAO.getAnnouncementIdByContestId(announcementRequest.getContestId());

        // Insert winners into the database
        for (AnnounceWinner winner : mapRequestToAnnouncement(announcementRequest).getWinners()) {
            announceWinnerDAO.insertWinner(
                    new AnnounceWinner(
                            announcementId,
                            winner.getContestEntryId(),
                            winner.getRanking()
                    )
            );
        }
    }

    private Announcement mapRequestToAnnouncement(AnnouncementRequest request) {
        Announcement announcement = new Announcement();
        announcement.setTitle(request.getTitle());
        announcement.setAnnouncementDate(new Date(System.currentTimeMillis()));
        announcement.setDescription(request.getDescription());
        announcement.setContestId(request.getContestId());
        announcement.setWinners(request.getWinners());

        return announcement;
    }

    private void validateAnnouncementRequest(AnnouncementRequest request) throws Exception {
        if (!contestDAO.existsById(request.getContestId())) {
            throw new Exception("Contest with ID " + request.getContestId() + " does not exist.");
        }

        if (announcementDAO.existsAnnouncementWithContest(request.getContestId())) {
            throw new Exception("An announcement already exists for contest ID " + request.getContestId() + ".");
        }
    }

}
