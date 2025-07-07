package com.ntn.culinary.dao;

import com.ntn.culinary.model.Contest;

import java.util.List;

public interface ContestDao {
    List<Contest> getAllContests();

    Contest getContestById(int id);

    boolean existsById(int id);
}
