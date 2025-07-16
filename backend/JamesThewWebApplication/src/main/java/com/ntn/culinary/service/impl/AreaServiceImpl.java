package com.ntn.culinary.service.impl;

import com.ntn.culinary.dao.AreaDao;
import com.ntn.culinary.exception.ConflictException;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.model.Area;
import com.ntn.culinary.request.AreaRequest;
import com.ntn.culinary.response.AreaResponse;
import com.ntn.culinary.service.AreaService;

import java.util.List;

public class AreaServiceImpl implements AreaService {

    private final AreaDao areaDao;

    public AreaServiceImpl(AreaDao areaDao) {
        this.areaDao = areaDao;
    }

    @Override
    public void addArea(String name) {
        if (!areaDao.existsByName(name)) {
            areaDao.insertArea(name);
        } else {
            throw new ConflictException("Area with name '" + name + "' already exists.");
        }
    }

    @Override
    public void updateArea(AreaRequest areaRequest) {
        if (areaDao.existsById(areaRequest.getId())) {

            Area area = areaDao.getAreaById((areaRequest.getId()));
            if (!area.getName().equals(areaRequest.getName()) && areaDao.existsByName(areaRequest.getName())) {
                throw new ConflictException("Area with name '" + areaRequest.getName() + "' already exists.");
            }

            areaDao.updateArea(mapAreaRequestToArea(areaRequest));
        } else {
            throw new NotFoundException("Area with ID " + areaRequest.getId() + " does not exist.");
        }
    }

    @Override
    public List<AreaResponse> getAllAreas() {
        return areaDao.getAllAreas().stream()
                .map(
                        area -> new AreaResponse(
                                area.getId(),
                                area.getName()
                        ))
                .toList();
    }

    @Override
    public AreaResponse getAreaById(int id) {
        if (areaDao.existsById(id)) {
            Area area = areaDao.getAreaById(id);
            return new AreaResponse(area.getId(), area.getName());
        } else {
            throw new NotFoundException("Area with ID " + id + " does not exist.");
        }
    }

    @Override
    public AreaResponse getAreaByName(String name) {
        Area area = areaDao.getAreaByName(name);
        if (area != null) {
            return new AreaResponse(area.getId(), area.getName());
        } else {
            throw new NotFoundException("Area with name '" + name + "' does not exist.");
        }
    }

    @Override
    public void deleteAreaById(int id) {
        if (areaDao.existsById(id)) {
            areaDao.deleteAreaById(id);
        } else {
            throw new NotFoundException("Area with ID " + id + " does not exist.");
        }
    }

    public Area mapAreaRequestToArea(AreaRequest areaRequest) {
        Area area = new Area();
        area.setId(areaRequest.getId());
        area.setName(areaRequest.getName());
        return area;
    }
}
