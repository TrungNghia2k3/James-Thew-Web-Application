package com.ntn.culinary.dao;

import com.ntn.culinary.model.Area;
import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AreaDAO {

    private static final AreaDAO areaDAO = new AreaDAO();

    private AreaDAO() {
        // Private constructor to prevent instantiation
    }

    public static AreaDAO getInstance() {
        return areaDAO;
    }

    private static final String CHECK_AREA_EXISTS_BY_NAME_QUERY = "SELECT 1 FROM areas WHERE name = ? LIMIT 1";
    private static final String INSERT_AREA_QUERY = "INSERT INTO areas (name) VALUES (?)";
    private static final String SELECT_ALL_AREAS_QUERY = "SELECT * FROM areas";
    private static final String SELECT_AREA_BY_ID_QUERY = "SELECT * FROM areas WHERE id = ?";

    public boolean existsByName(String name) throws SQLException {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_AREA_EXISTS_BY_NAME_QUERY)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true nếu tồn tại ít nhất 1 dòng
            } catch (SQLException e) {
                throw e; // Ném lại ngoại lệ để xử lý ở nơi gọi
            }
        }
    }

    public void insertArea(String name) throws SQLException {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_AREA_QUERY)) {
            stmt.setString(1, name.toUpperCase());
            stmt.executeUpdate();
        }
    }

    public List<Area> getAllAreas() throws SQLException {
        Connection conn = DatabaseUtils.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_AREAS_QUERY);

        try (ResultSet rs = stmt.executeQuery()) {
            List<Area> areas = new ArrayList<>();
            while (rs.next()) {
                Area area = new Area();
                area.setId(rs.getInt("id"));
                area.setName(rs.getString("name"));
                areas.add(area);
            }
            return areas;
        }
    }

    public Area getAreaById(int id) throws SQLException {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_AREA_BY_ID_QUERY)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Area area = new Area();
                    area.setId(rs.getInt("id"));
                    area.setName(rs.getString("name"));
                    return area;
                } else {
                    return null; // Không tìm thấy area với id này
                }
            }
        }
    }
}
