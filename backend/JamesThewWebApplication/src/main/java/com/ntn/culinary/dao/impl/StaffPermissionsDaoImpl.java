package com.ntn.culinary.dao.impl;

import com.ntn.culinary.dao.StaffPermissionsDao;
import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StaffPermissionsDaoImpl implements StaffPermissionsDao {
    public void assignPermissionToStaff(int staffId, int permissionId) {
        String INSERT_PERMISSION_QUERY = "INSERT INTO staff_permissions (staff_id, permission_id) VALUES (?, ?)";

        // Insert the new staff-permission association
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_PERMISSION_QUERY)) {
            stmt.setInt(1, staffId);
            stmt.setInt(2, permissionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error assigning permission to staff", e);
        }
    }

    public void removePermissionFromStaff(int staffId, int permissionId) {

        String DELETE_PERMISSION_QUERY = "DELETE FROM staff_permissions WHERE staff_id = ? AND permission_id = ?";

        // Delete the staff-permission association
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_PERMISSION_QUERY)) {
            stmt.setInt(1, staffId);
            stmt.setInt(2, permissionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing permission from staff", e);
        }
    }

    public boolean existsStaffPermission(int staffId, int permissionId) {

        String CHECK_PERMISSION_EXISTS_QUERY = "SELECT 1 FROM staff_permissions WHERE staff_id = ? AND permission_id = ? LIMIT 1";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_PERMISSION_EXISTS_QUERY)) {
            stmt.setInt(1, staffId);
            stmt.setInt(2, permissionId);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking permission exists", e);
        }
    }
}
