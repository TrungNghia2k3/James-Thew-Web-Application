package com.ntn.culinary.dao;

import com.ntn.culinary.model.ContestEntry;

public interface ContestEntryDao {
    void addContestEntry(ContestEntry contestEntry);

    int getContestEntryIdByUserIdAndContestId(int userId, int contestId);

    boolean existsByUserIdAndContestIdAndName(int userId, int contestId, String name);

    void updateContestEntryStatus(int contestEntryId, String status);

    boolean existsById(int contestEntryId);

    ContestEntry getContestEntryById(int contestEntryId);
}
