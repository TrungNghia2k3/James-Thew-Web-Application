package com.dao;

import com.exception.UserNotFoundException;
import com.model.Permission;
import com.model.Role;
import com.constant.RoleType;
import com.model.User;
import com.request.UserRequest;
import com.response.UserResponse;
import com.config.DatabaseConfig;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class UserDAO {

    // ------------------- SQL QUERIES -------------------
    private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";

    // ------------------- INSERT QUERIES -------------------
    private static final String INSERT_USER_QUERY = "INSERT INTO users (username, password, email, first_name, last_name, phone, created_at, is_active) VALUES (?, ?, ?, ?, ?, ?, NOW(), true)";
    private static final String GET_USER_ID = "SELECT id FROM users WHERE username = ?";
    private static final String GET_ROLE_ID = "SELECT id FROM roles WHERE name = ?";
    private static final String INSERT_USER_ROLE = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";

    private static final String UPDATE_USER_QUERY = "UPDATE users SET username = ?, password = ?, email = ?, role = ?, subscription_type = ?, subscription_start_date = ?, subscription_end_date = ? WHERE user_id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE user_id = ?";
    private static final String SELECT_USER_BY_USERNAME_QUERY =
            "SELECT u.id AS user_id, u.username, u.password, u.is_active, " +
                    "r.id AS role_id, r.name AS role_name, " +
                    "p.id AS permission_id, p.name AS permission_name " +
                    "FROM users u " +
                    "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
                    "LEFT JOIN roles r ON ur.role_id = r.id " +
                    "LEFT JOIN staff_permissions sp ON u.id = sp.user_id " +
                    "LEFT JOIN permissions p ON sp.permission_id = p.id " +
                    "WHERE u.username = ?";
    private static final String SELECT_SUBSCRIPTION_QUERY = "SELECT subscription_end_date FROM users WHERE user_id = ?";
    private static final String CHECK_USER_EXISTS_QUERY = "SELECT 1 FROM users WHERE user_id = ?";
    private static final String CHECK_USER_ID_EXISTS_QUERY = "SELECT 1 FROM users WHERE id = ?";
    // ------------------- CRUD OPERATIONS -------------------

    public boolean existsById(int id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_USER_ID_EXISTS_QUERY)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true nếu tồn tại user có id đó
            }
        }
    }


    public List<UserResponse> getAllUsers() throws SQLException {
        List<UserResponse> users = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_USERS_QUERY);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UserResponse user = new UserResponse();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setSubscriptionType(rs.getString("subscription_type"));
                user.setSubscriptionStartDate(rs.getDate("subscription_start_date"));
                user.setSubscriptionEndDate(rs.getDate("subscription_end_date"));

                users.add(user);
            }
        }

        return users;
    }

    public UserResponse getUserById(int id) throws UserNotFoundException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USER_BY_ID_QUERY)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UserResponse user = new UserResponse();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setSubscriptionType(rs.getString("subscription_type"));
                    user.setSubscriptionStartDate(rs.getDate("subscription_start_date"));
                    user.setSubscriptionEndDate(rs.getDate("subscription_end_date"));
                    return user;
                } else {
                    throw new UserNotFoundException("User with id " + id + " not found.");
                }
            }

        } catch (SQLException e) {
            throw new UserNotFoundException("Error while fetching user with id " + id + ": " + e.getMessage());
        }
    }

    public void addUser(UserRequest user) throws SQLException {

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Insert user
            try (PreparedStatement insertUserStmt = conn.prepareStatement(INSERT_USER_QUERY)) {
                insertUserStmt.setString(1, user.getUsername());
                insertUserStmt.setString(2, hashedPassword);
                insertUserStmt.setString(3, user.getEmail());
                insertUserStmt.setString(4, user.getFirstName());
                insertUserStmt.setString(5, user.getLastName());
                insertUserStmt.setString(6, user.getPhone());

                insertUserStmt.executeUpdate();
            }

            // 2. Get user_id
            int userId;
            try (PreparedStatement getUserIdStmt = conn.prepareStatement(GET_USER_ID)) {
                getUserIdStmt.setString(1, user.getUsername());
                try (ResultSet rs = getUserIdStmt.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getInt("id");
                    } else {
                        conn.rollback();
                        throw new SQLException("Cannot find newly inserted user");
                    }
                }
            }

            // 3. Get role_id for GENERAL
            int roleId;
            try (PreparedStatement getRoleIdStmt = conn.prepareStatement(GET_ROLE_ID)) {
                getRoleIdStmt.setString(1, String.valueOf(RoleType.GENERAL));
                try (ResultSet rs = getRoleIdStmt.executeQuery()) {
                    if (rs.next()) {
                        roleId = rs.getInt("id");
                    } else {
                        conn.rollback();
                        throw new SQLException("Role 'GENERAL' not found");
                    }
                }
            }

            // 4. Insert user_roles
            try (PreparedStatement insertUserRoleStmt = conn.prepareStatement(INSERT_USER_ROLE)) {
                insertUserRoleStmt.setInt(1, userId);
                insertUserRoleStmt.setInt(2, roleId);
                insertUserRoleStmt.executeUpdate();
            }

            conn.commit(); // Commit nếu mọi thứ thành công
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("users.username")) {
                throw new SQLException("Username '" + user.getUsername() + "' already exists");
            } else if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("users.email")) {
                throw new SQLException("Email '" + user.getEmail() + "' already exists");
            } else {
                throw new SQLException("Error adding user and assigning role: " + e.getMessage(), e);
            }
        }
    }


//    public void editUser(UserRequest user) throws SQLException, UserNotFoundException {
//        if (userExists(user.getId())) {
//            throw new UserNotFoundException("User with id " + user.getId() + " not found.");
//        }
//
//        // Encrypt the password before storing
//        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
//
//        try (Connection conn = DBUtil.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER_QUERY)) {
//
//            stmt.setString(1, user.getUsername());
//            stmt.setString(2, hashedPassword);
//            stmt.setString(3, user.getEmail());
//            stmt.setString(4, user.getRole());
//            stmt.setString(5, user.getSubscriptionType());
//            stmt.setDate(6, new java.sql.Date(user.getSubscriptionStartDate().getTime()));
//            stmt.setDate(7, new java.sql.Date(user.getSubscriptionEndDate().getTime()));
//            stmt.setInt(8, user.getId());
//
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            throw new SQLException("Error while updating user: " + e.getMessage(), e);
//        }
//    }

    public void deleteUser(int id) throws SQLException, UserNotFoundException {
        if (userExists(id)) {
            throw new UserNotFoundException("User with id " + id + " not found.");
        }

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER_QUERY)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error while deleting user: " + e.getMessage(), e);
        }
    }

    // ------------------- AUTHENTICATION & SUBSCRIPTION -------------------

    public User findUserByUsername(String username) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USER_BY_USERNAME_QUERY)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            User user = null;
            Set<Integer> roleIds = new HashSet<>();
            Set<Integer> permIds = new HashSet<>();

            while (rs.next()) {
                if (user == null) {
                    user = new User();
                    user.setId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setActive(rs.getBoolean("is_active"));
                }

                int roleId = rs.getInt("role_id");
                if (!rs.wasNull() && roleIds.add(roleId)) {
                    Role role = new Role();
                    role.setId(roleId);
                    role.setName(rs.getString("role_name"));
                    user.getRoles().add(role);
                }

                int permId = rs.getInt("permission_id");
                if (!rs.wasNull() && permIds.add(permId)) {
                    Permission perm = new Permission();
                    perm.setId(permId);
                    perm.setName(rs.getString("permission_name"));
                    user.getPermissions().add(perm);
                }
            }

            return user;
        } catch (SQLException e) {
            throw new SQLException("Error while fetching user by username: " + e.getMessage());
        }
    }

    public boolean isSubscriptionValid(int userId) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_SUBSCRIPTION_QUERY)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Date endDate = rs.getDate("subscription_end_date");
                return endDate != null && !endDate.before(new Date());
            }

            return false;
        } catch (SQLException e) {
            throw new SQLException("Error while checking subscription validity: " + e.getMessage(), e);
        }
    }

    // ------------------- UTILITIES -------------------

    private boolean userExists(int id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_USER_EXISTS_QUERY)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return !rs.next(); // true if user exists
            }

        } catch (SQLException e) {
            return true; // If there's an error, we assume the user doesn't exist
        }
    }
}


