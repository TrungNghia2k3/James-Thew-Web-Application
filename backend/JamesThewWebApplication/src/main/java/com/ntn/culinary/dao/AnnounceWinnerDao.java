package com.ntn.culinary.dao;

import com.ntn.culinary.model.AnnounceWinner;
import com.ntn.culinary.model.Announcement;

import java.util.List;

public interface AnnounceWinnerDao {
    void insertWinner(AnnounceWinner announceWinner);

    boolean existsWinner(int announcementId, int contestEntryId);

    void updateWinner(AnnounceWinner announceWinner);

    void deleteWinner(int announcementId, int contestEntryId);

    List<AnnounceWinner> getAllWinnersByAnnouncementId(int announcementId);
}
