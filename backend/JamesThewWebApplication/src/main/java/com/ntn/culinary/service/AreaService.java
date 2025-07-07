package com.ntn.culinary.service;

import com.ntn.culinary.dao.AreaDao;
import com.ntn.culinary.exception.ConflictException;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.model.Area;
import com.ntn.culinary.response.AreaResponse;

import java.util.List;

public class AreaService {

    private final AreaDao areaDao;

    public AreaService(AreaDao areaDao) {
        this.areaDao = areaDao;
    }

    public void addArea(String name) {
        if (!areaDao.existsByName(name)) {
            areaDao.insertArea(name);
        } else {
            throw new ConflictException("Area with name '" + name + "' already exists.");
        }
    }

    public List<AreaResponse> getAllAreas() {
        return areaDao.getAllAreas().stream()
                .map(
                        area -> new AreaResponse(
                                area.getId(),
                                area.getName()
                        ))
                .toList();
    }

    public AreaResponse getAreaById(int id) {
        Area area = areaDao.getAreaById(id);

        if (area == null) {
            throw new NotFoundException("Area with ID " + id + " does not exist.");
        }

        return new AreaResponse(area.getId(), area.getName());
    }
}
