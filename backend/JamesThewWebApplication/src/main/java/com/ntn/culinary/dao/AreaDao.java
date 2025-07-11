package com.ntn.culinary.dao;

import com.ntn.culinary.model.Area;

import java.util.List;

public interface AreaDao {

    boolean existsByName(String name);

    void insertArea(String name);

    void updateArea(Area area);

    List<Area> getAllAreas();

    Area getAreaById(int id);

    void deleteAreaById(int id);

    boolean existsById(int id);

    Area getAreaByName(String name);
}
