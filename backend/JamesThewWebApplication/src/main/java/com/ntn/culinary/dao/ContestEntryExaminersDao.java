package com.ntn.culinary.dao;

import com.ntn.culinary.model.ContestEntryExaminers;

public interface ContestEntryExaminersDao {
    void addContestEntryExaminer(ContestEntryExaminers contestEntryExaminers);

    boolean existsByContestEntryIdAndExaminerId(int contestEntryId, int examinerId);
}
