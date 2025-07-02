package com.ntn.culinary.dao;

import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.SQLException;

public class StaffPermissionsDAO {
    private static final StaffPermissionsDAO staffPermissionsDAO = new StaffPermissionsDAO();

    private StaffPermissionsDAO() {
        // Private constructor to prevent instantiation
    }

    public static StaffPermissionsDAO getInstance() {
        return staffPermissionsDAO;
    }

    // Define methods for managing staff permissions here
    // For example, methods to check permissions, assign permissions, etc.

    private static final String CHECK_PERMISSION_EXISTS_QUERY = "SELECT 1 FROM staff_permissions WHERE staff_id = ? AND permission_id = ? LIMIT 1";

    private static final String INSERT_PERMISSION_QUERY = "INSERT INTO staff_permissions (staff_id, permission_id) VALUES (?, ?)";

    private static final String DELETE_PERMISSION_QUERY = "DELETE FROM staff_permissions WHERE staff_id = ? AND permission_id = ?";

    public void assignPermissionToStaff(int staffId, int permissionId) throws SQLException {
        // Check if the staff already has the permission
        if (existsStaffPermission(staffId, permissionId)) {
            throw new SQLException("Staff already has this permission");
        }

        // Insert the new staff-permission association
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(INSERT_PERMISSION_QUERY)) {
            stmt.setInt(1, staffId);
            stmt.setInt(2, permissionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error assigning permission to staff", e);
        }
    }

    public void removePermissionFromStaff(int staffId, int permissionId) throws SQLException {
        // Check if the staff has the permission before attempting to remove it
        if (!existsStaffPermission(staffId, permissionId)) {
            throw new SQLException("Staff does not have this permission");
        }

        // Delete the staff-permission association
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(DELETE_PERMISSION_QUERY)) {
            stmt.setInt(1, staffId);
            stmt.setInt(2, permissionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error removing permission from staff", e);
        }
    }

    private boolean existsStaffPermission(int staffId, int permissionId) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(CHECK_PERMISSION_EXISTS_QUERY)) {
            stmt.setInt(1, staffId);
            stmt.setInt(2, permissionId);
            try (var rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
