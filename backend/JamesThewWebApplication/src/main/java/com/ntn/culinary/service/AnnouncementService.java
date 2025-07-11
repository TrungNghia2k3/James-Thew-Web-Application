package com.ntn.culinary.service;

import com.ntn.culinary.dao.AnnounceWinnerDao;
import com.ntn.culinary.dao.AnnouncementDao;
import com.ntn.culinary.dao.ContestDao;
import com.ntn.culinary.dao.ContestEntryDao;
import com.ntn.culinary.exception.ConflictException;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.model.AnnounceWinner;
import com.ntn.culinary.model.Announcement;
import com.ntn.culinary.request.AnnouncementRequest;
import com.ntn.culinary.response.AnnounceWinnerResponse;
import com.ntn.culinary.response.AnnouncementResponse;

import java.sql.Date;
import java.util.List;

public class AnnouncementService {

    private final ContestDao contestDao;
    private final AnnouncementDao announcementDao;
    private final AnnounceWinnerDao announceWinnerDao;
    private final ContestEntryDao contestEntryDao;

    public AnnouncementService(ContestDao contestDao, AnnouncementDao announcementDao, AnnounceWinnerDao announceWinnerDao, ContestEntryDao contestEntryDao) {
        this.contestDao = contestDao;
        this.announcementDao = announcementDao;
        this.announceWinnerDao = announceWinnerDao;
        this.contestEntryDao = contestEntryDao;
    }

    public List<AnnouncementResponse> getAllAnnouncements() {
        // Fetch all announcements from the database
        return announcementDao.getAllAnnouncements().stream()
                .map(this::mapAnnouncementToResponse)
                .toList();
    }

    public AnnouncementResponse getAnnouncementById(int announcementId) {

        // Check if the announcement exists
        if (!announcementDao.existsAnnouncementById(announcementId)) {
            throw new NotFoundException("Announcement with ID " + announcementId + " does not exist.");
        }

        // Fetch the announcement by contest ID
        Announcement announcement = announcementDao.getAnnouncementById(announcementId);
        return mapAnnouncementToResponse(announcement);
    }

    public void addAnnouncement(AnnouncementRequest announcementRequest) {

        // Validate the announcement request
        validateAnnouncementRequest(announcementRequest);

        if (announcementDao.existsAnnouncementWithContest(announcementRequest.getContestId())) {
            throw new ConflictException("Announcement already exists");
        }

        // Insert the announcement into the database
        announcementDao.insertAnnouncement(mapRequestToAnnouncement(announcementRequest));

        // get the announcement ID
        Integer announcementId = announcementDao
                .getAnnouncementIdByContestId(announcementRequest.getContestId())
                .orElseThrow(() -> new NotFoundException("Announcement not found for contest"));

        // Insert winners into the database
        for (AnnounceWinner winner : mapRequestToAnnouncement(announcementRequest).getWinners()) {
            announceWinnerDao.insertWinner(
                    new AnnounceWinner(
                            announcementId,
                            winner.getContestEntryId(),
                            winner.getRanking()
                    )
            );
        }
    }

    public void updateAnnouncement(AnnouncementRequest announcementRequest) {
        // Validate the announcement request
        validateAnnouncementRequest(announcementRequest);

        if (!announcementDao.existsAnnouncementById(announcementRequest.getId())) {
            throw new NotFoundException("Announcement with ID " + announcementRequest.getId() + " does not exist.");
        }

        // Update the announcement in the database
        Announcement announcement = mapRequestToAnnouncement(announcementRequest);
        announcementDao.updateAnnouncement(announcement);

        // Update winners in the database
        for (AnnounceWinner winner : announcement.getWinners()) {
            if (announceWinnerDao.existsWinner(announcement.getId(), winner.getContestEntryId())) {
                announceWinnerDao.updateWinner(winner);
            } else {
                announceWinnerDao.insertWinner(winner);
            }
        }
    }

    public void deleteAnnouncement(int announcementId) {
        // Check if the announcement exists
        if (announcementDao.existsAnnouncementById(announcementId)) {

            // Delete winners associated with the announcement
            List<AnnounceWinner> winners = announceWinnerDao.getAllWinnersByAnnouncementId(announcementId);
            if (!winners.isEmpty()) {
                winners.forEach(winner -> announceWinnerDao.deleteWinner(announcementId, winner.getContestEntryId()));
            }

            // Delete the announcement from the database
            announcementDao.deleteAnnouncementById(announcementId);
        } else {
            throw new NotFoundException("Announcement with ID " + announcementId + " does not exist.");
        }
    }

    public void deleteWinnersByAnnouncementId(int announcementId) {
        // Check if the announcement exists
        if (!announcementDao.existsAnnouncementById(announcementId)) {
            throw new NotFoundException("Announcement with ID " + announcementId + " does not exist.");
        }

        // Delete winners associated with the announcement
        announceWinnerDao.getAllWinnersByAnnouncementId(announcementId)
                .forEach(winner -> announceWinnerDao.deleteWinner(announcementId, winner.getContestEntryId()));
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

    private AnnouncementResponse mapAnnouncementToResponse(Announcement announcement) {
        AnnouncementResponse response = new AnnouncementResponse();
        response.setId(announcement.getId());
        response.setTitle(announcement.getTitle());
        response.setAnnouncementDate(announcement.getAnnouncementDate());
        response.setDescription(announcement.getDescription());
        response.setContest(contestDao.getContestById(announcement.getContestId()));
        response.setWinners(
                announceWinnerDao.getAllWinnersByAnnouncementId(announcement.getId())
                        .stream()
                        .map(winner ->
                                new AnnounceWinnerResponse(
                                        winner.getId(),
                                        contestEntryDao.getContestEntryById(winner.getContestEntryId()),
                                        winner.getRanking()))
                        .toList()
        );

        return response;
    }

    private void validateAnnouncementRequest(AnnouncementRequest request) {
        if (!contestDao.existsById(request.getContestId())) {
            throw new NotFoundException("Contest with ID " + request.getContestId() + " does not exist.");
        }
    }
}
