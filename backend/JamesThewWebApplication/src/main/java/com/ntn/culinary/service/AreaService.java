package com.ntn.culinary.service;

import com.ntn.culinary.dao.AreaDAO;
import com.ntn.culinary.model.Area;
import com.ntn.culinary.response.AreaResponse;

import java.sql.SQLException;
import java.util.List;

public class AreaService {
    private static final AreaService areaService = new AreaService();

    private AreaService() {
        // Private constructor to prevent instantiation
    }

    public static AreaService getInstance() {
        return areaService;
    }

    private final AreaDAO areaDAO = AreaDAO.getInstance();

    public boolean checkAreaExists(String name) throws SQLException {
        return areaDAO.existsByName(name);
    }

    public void addArea(String name) throws SQLException {
        if (!checkAreaExists(name)) {
            try {
                areaDAO.insertArea(name);
            } catch (SQLException e) {
                // Log the error or handle it as needed
                throw new SQLException("Error inserting area: " + e.getMessage(), e);
            }
        } else {
            throw new IllegalArgumentException("Area with name '" + name + "' already exists.");
        }
    }

    public List<AreaResponse> getAllAreas() throws SQLException {
        return areaDAO.getAllAreas().stream()
                .map(
                        area -> new AreaResponse(
                                area.getId(),
                                area.getName()
                        ))
                .toList();
    }

    public AreaResponse getAreaById(int id) throws SQLException {
        Area area = areaDAO.getAreaById(id);
        if (area != null) {
            return new AreaResponse(
                    area.getId(),
                    area.getName()
            );
        }
        return null; // or throw an exception if preferred
    }
}
