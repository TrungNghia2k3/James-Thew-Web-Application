package com.ntn.culinary.dao;

import com.ntn.culinary.model.Role;
import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {
    private static final RoleDAO roleDAO = new RoleDAO();

    private RoleDAO() {
        // Private constructor to prevent instantiation
    }

    public static RoleDAO getInstance() {
        return roleDAO;
    }

    private static final String CHECK_ROLE_EXISTS_BY_NAME_QUERY = "SELECT 1 FROM roles WHERE name = ? LIMIT 1";

    private static final String SELECT_ALL_ROLES_QUERY = "SELECT * FROM roles";

    private static final String SELECT_ROLE_BY_ID_QUERY = "SELECT * FROM roles WHERE id = ?";

    private static final String EXISTS_ROLE_BY_ID_QUERY = "SELECT 1 FROM roles WHERE id = ? LIMIT 1";

    public boolean existsByName(String name) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(CHECK_ROLE_EXISTS_BY_NAME_QUERY)) {

            stmt.setString(1, name);
            try (var rs = stmt.executeQuery()) {
                return rs.next(); // true if at least one row exists
            }
        }
    }

    public boolean existsById(int id) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(EXISTS_ROLE_BY_ID_QUERY)) {

            stmt.setInt(1, id);
            try (var rs = stmt.executeQuery()) {
                return rs.next(); // true if at least one row exists
            }
        }
    }

    public List<Role> getAllRoles() throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(SELECT_ALL_ROLES_QUERY);
             var rs = stmt.executeQuery()) {

            List<Role> roles = new ArrayList<>();
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setName(rs.getString("name"));
                roles.add(role);
            }
            return roles;
        }
    }

    public Role getRoleById(int id) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(SELECT_ROLE_BY_ID_QUERY)) {

            stmt.setInt(1, id);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role();
                    role.setId(rs.getInt("id"));
                    role.setName(rs.getString("name"));
                    return role;
                } else {
                    return null; // No role found with the given ID
                }
            }
        }
    }
}
