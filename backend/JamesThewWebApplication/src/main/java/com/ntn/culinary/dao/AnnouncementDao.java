package com.ntn.culinary.dao;

import com.ntn.culinary.model.Announcement;

import java.util.List;
import java.util.Optional;

public interface AnnouncementDao {
    void insertAnnouncement(Announcement announcement);

    boolean existsAnnouncementWithContest(int contestId);

    boolean existsAnnouncementById(int id);

    Optional<Integer> getAnnouncementIdByContestId (int contestId);

    List<Announcement> getAllAnnouncements();

    Announcement getAnnouncementById(int id);

    void updateAnnouncement(Announcement announcement);

    void deleteAnnouncementById(int id);
}
