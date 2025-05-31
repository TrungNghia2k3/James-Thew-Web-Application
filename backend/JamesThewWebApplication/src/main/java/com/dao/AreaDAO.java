package com.dao;

import com.config.DatabaseConfig;
import com.model.DatabaseAuditLog;
import com.model.UserActionAuditLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AreaDAO {
    private static final DatabaseAuditLogDAO logDAO = new DatabaseAuditLogDAO();

    private static final String CHECK_AREA_EXISTS_BY_NAME_QUERY = "SELECT 1 FROM areas WHERE name = ? LIMIT 1";
    private static final String INSERT_AREA_QUERY = "INSERT INTO areas (name) VALUES (?)";


    public boolean existsByName(String name) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_AREA_EXISTS_BY_NAME_QUERY)) {

            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {

                DatabaseAuditLog log = new DatabaseAuditLog();
                log.setUserId(1); // Giả sử user ID là 1
                log.setOperation("SELECT");
                log.setTableName("areas");
                log.setDetails("SELECT 1 FROM areas WHERE name = '" + name + "'");
                log.setStatus("SUCCESS");
                logDAO.insertDatabaseLog(log);

                return rs.next(); // true nếu tồn tại ít nhất 1 dòng
            } catch (SQLException e) {
                // Ghi log lỗi nếu có
                DatabaseAuditLog log = new DatabaseAuditLog();
                log.setUserId(1); // Giả sử user ID là 1
                log.setOperation("SELECT");
                log.setTableName("areas");
                log.setDetails("SELECT 1 FROM areas WHERE name = '" + name + "'");
                log.setStatus("FAILURE");
                log.setDescription("Error checking area existence: " + e.getMessage());

                logDAO.insertDatabaseLog(log);
                throw e; // Ném lại ngoại lệ để xử lý ở nơi gọi
            }
        }
    }

    public void insertArea(String name) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();

             PreparedStatement stmt = conn.prepareStatement(INSERT_AREA_QUERY)) {

            stmt.setString(1, name.toUpperCase());
            stmt.executeUpdate();

            DatabaseAuditLog log = new DatabaseAuditLog();
            log.setUserId(1); // Giả sử user ID là 1
            log.setOperation("INSERT");
            log.setTableName("areas");
            log.setDetails("Inserted area with name: " + name);
            log.setStatus("SUCCESS");
            logDAO.insertDatabaseLog(log);

        } catch (SQLException e) {
            // Ghi log lỗi nếu có
            DatabaseAuditLog log = new DatabaseAuditLog();
            log.setUserId(1); // Giả sử user ID là 1
            log.setOperation("INSERT");
            log.setTableName("areas");
            log.setDetails("Failed to insert area with name: " + name);
            log.setStatus("FAILURE");
            log.setDescription("Error inserting area: " + e.getMessage());

            logDAO.insertDatabaseLog(log);

            throw e; // Ném lại ngoại lệ để xử lý ở nơi gọi
        }
    }
}
