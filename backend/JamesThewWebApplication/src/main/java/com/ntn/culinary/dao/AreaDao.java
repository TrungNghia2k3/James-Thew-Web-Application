package com.ntn.culinary.dao;

import com.ntn.culinary.model.Area;

import java.util.List;

public interface AreaDao {

    boolean existsByName(String name);

    void insertArea(String name);

    List<Area> getAllAreas();

    Area getAreaById(int id);
}
