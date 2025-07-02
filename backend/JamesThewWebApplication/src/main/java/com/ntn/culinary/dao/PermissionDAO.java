package com.ntn.culinary.dao;

import com.ntn.culinary.model.Permission;
import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PermissionDAO {
    private static final PermissionDAO permissionDAO = new PermissionDAO();

    private PermissionDAO() {
        // Private constructor to prevent instantiation
    }

    public static PermissionDAO getInstance() {
        return permissionDAO;
    }

    private static final String CHECK_PERMISSION_EXISTS_BY_NAME_QUERY = "SELECT 1 FROM permissions WHERE name = ? LIMIT 1";

    private static final String SELECT_ALL_PERMISSIONS_QUERY = "SELECT * FROM permissions";

    private static final String SELECT_PERMISSION_BY_ID_QUERY = "SELECT * FROM permissions WHERE id = ?";

    public boolean existsByName(String name) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(CHECK_PERMISSION_EXISTS_BY_NAME_QUERY)) {

            stmt.setString(1, name);
            try (var rs = stmt.executeQuery()) {
                return rs.next(); // true if at least one row exists
            }
        }
    }

    public List<Permission> getAllPermissions() throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(SELECT_ALL_PERMISSIONS_QUERY);
             var rs = stmt.executeQuery()) {

            List<Permission> permissions = new ArrayList<>();
            while (rs.next()) {
                Permission permission = new Permission();

                permission.setId(rs.getInt("id"));
                permission.setName(rs.getString("name"));
                permission.setDescription(rs.getString("description"));

                permissions.add(permission);
            }
            return permissions;
        }
    }

    public Permission getPermissionById(int id) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(SELECT_PERMISSION_BY_ID_QUERY)) {

            stmt.setInt(1, id);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Permission permission = new Permission();

                    permission.setId(rs.getInt("id"));
                    permission.setName(rs.getString("name"));
                    permission.setDescription(rs.getString("description"));

                    return permission;
                } else {
                    return null; // No permission found with the given ID
                }
            }
        }
    }
}
