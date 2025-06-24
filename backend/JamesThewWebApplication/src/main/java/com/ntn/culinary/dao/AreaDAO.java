package com.ntn.culinary.dao;

import com.ntn.culinary.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AreaDAO {

    private static final String CHECK_AREA_EXISTS_BY_NAME_QUERY = "SELECT 1 FROM areas WHERE name = ? LIMIT 1";
    private static final String INSERT_AREA_QUERY = "INSERT INTO areas (name) VALUES (?)";


    public boolean existsByName(String name) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
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
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_AREA_QUERY)) {
            stmt.setString(1, name.toUpperCase());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e; // Ném lại ngoại lệ để xử lý ở nơi gọi
        }
    }
}
