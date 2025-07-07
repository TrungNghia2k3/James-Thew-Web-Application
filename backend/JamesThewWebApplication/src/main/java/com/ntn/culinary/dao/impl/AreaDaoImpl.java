package com.ntn.culinary.dao.impl;

import com.ntn.culinary.dao.AreaDao;
import com.ntn.culinary.model.Area;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.ntn.culinary.utils.DatabaseUtils.getConnection;

public class AreaDaoImpl implements AreaDao {
    @Override
    public boolean existsByName(String name) {

        String CHECK_AREA_EXISTS_BY_NAME_QUERY = "SELECT 1 FROM areas WHERE name = ? LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_AREA_EXISTS_BY_NAME_QUERY)) {
            stmt.setString(1, name);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking area existence: " + e.getMessage(), e);
        }
    }

    @Override
    public void insertArea(String name) {

        String INSERT_AREA_QUERY = "INSERT INTO areas (name) VALUES (?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_AREA_QUERY)) {
            stmt.setString(1, name.toUpperCase());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking winner existence: " + e.getMessage(), e);
        }

    }

    @Override
    public List<Area> getAllAreas() {

        String SELECT_ALL_AREAS_QUERY = "SELECT * FROM areas";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_AREAS_QUERY)) {

            List<Area> areas = new ArrayList<>();

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Area area = new Area();
                    area.setId(rs.getInt("id"));
                    area.setName(rs.getString("name"));
                    areas.add(area);
                }
                return areas;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking winner existence: " + e.getMessage(), e);
        }
    }

    @Override
    public Area getAreaById(int id) {

        String SELECT_AREA_BY_ID_QUERY = "SELECT * FROM areas WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_AREA_BY_ID_QUERY)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Area area = new Area();
                    area.setId(rs.getInt("id"));
                    area.setName(rs.getString("name"));
                    return area;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking winner existence: " + e.getMessage(), e);
        }
    }
}

