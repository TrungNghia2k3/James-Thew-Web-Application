package com.ntn.culinary.dao;

import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.SQLException;

public class UserRolesDAO {

    private static final UserRolesDAO userRolesDAO = new UserRolesDAO();

    private UserRolesDAO() {
        // Private constructor to prevent instantiation
    }

    public static UserRolesDAO getInstance() {
        return userRolesDAO;
    }

    private static final String CHECK_USER_ROLE_EXISTS_QUERY = "SELECT 1 FROM user_roles WHERE user_id = ? AND role_id = ? LIMIT 1";

    private static final String INSERT_USER_ROLE_QUERY = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";

    private static final String DELETE_USER_ROLE_QUERY = "DELETE FROM user_roles WHERE user_id = ? AND role_id = ?";

    public void assignRoleToUser(int userId, int roleId) throws SQLException {
        // Check if the user already has the role
        if (existsUserRole(userId, roleId)) {
            throw new SQLException("User already has this role");
        }

        // Insert the new user-role association
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(INSERT_USER_ROLE_QUERY)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, roleId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error assigning role to user", e);
        }
    }


    public void removeRoleFromUser(int userId, int roleId) throws SQLException {
        // Check if the user has the role before attempting to remove it
        if (!existsUserRole(userId, roleId)) {
            throw new SQLException("User does not have this role");
        }

        // Delete the user-role association
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(DELETE_USER_ROLE_QUERY)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, roleId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error removing role from user", e);
        }
    }

    private boolean existsUserRole(int userId, int roleId) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(CHECK_USER_ROLE_EXISTS_QUERY)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, roleId);
            try (var rs = stmt.executeQuery()) {
                return rs.next(); // true if at least one row exists
            }
        }
    }
}



