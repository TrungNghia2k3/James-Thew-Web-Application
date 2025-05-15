package com.dao;

import com.request.UserRequest;
import com.response.UserResponse;
import com.exception.UserNotFoundException;
import com.model.User;
import com.util.DBUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class UserDao {

    // ------------------- SQL QUERIES -------------------
    private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String INSERT_USER_QUERY = "INSERT INTO users (username, password, email, role, subscription_type, subscription_start_date, subscription_end_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET username = ?, password = ?, email = ?, role = ?, subscription_type = ?, subscription_start_date = ?, subscription_end_date = ? WHERE user_id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE user_id = ?";
    private static final String SELECT_USER_BY_USERNAME_QUERY = "SELECT user_id, username, role, password FROM users WHERE username = ?";
    private static final String SELECT_SUBSCRIPTION_QUERY = "SELECT subscription_end_date FROM users WHERE user_id = ?";
    private static final String CHECK_USER_EXISTS_QUERY = "SELECT 1 FROM users WHERE user_id = ?";

    // ------------------- CRUD OPERATIONS -------------------

    public List<UserResponse> getAllUsers() throws SQLException {
        List<UserResponse> users = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
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
        try (Connection conn = DBUtil.getConnection();
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
        // Encrypt the password before storing
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        // Force role to "member"
        String role = "member";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER_QUERY)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getEmail());
            stmt.setString(4, role);
            stmt.setString(5, user.getSubscriptionType());
            stmt.setDate(6, new java.sql.Date(user.getSubscriptionStartDate().getTime()));
            stmt.setDate(7, new java.sql.Date(user.getSubscriptionEndDate().getTime()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error while adding user: " + e.getMessage(), e);
        }
    }

    public void editUser(UserRequest user) throws SQLException, UserNotFoundException {
        if (userExists(user.getId())) {
            throw new UserNotFoundException("User with id " + user.getId() + " not found.");
        }

        // Encrypt the password before storing
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER_QUERY)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getSubscriptionType());
            stmt.setDate(6, new java.sql.Date(user.getSubscriptionStartDate().getTime()));
            stmt.setDate(7, new java.sql.Date(user.getSubscriptionEndDate().getTime()));
            stmt.setInt(8, user.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error while updating user: " + e.getMessage(), e);
        }
    }

    public void deleteUser(int id) throws SQLException, UserNotFoundException {
        if (userExists(id)) {
            throw new UserNotFoundException("User with id " + id + " not found.");
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER_QUERY)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error while deleting user: " + e.getMessage(), e);
        }
    }

    // ------------------- AUTHENTICATION & SUBSCRIPTION -------------------

    public User findUserByUsername(String username) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USER_BY_USERNAME_QUERY)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("role"),
                        rs.getString("password")
                );
            }

            return null;
        } catch (SQLException e) {
            throw new SQLException("Error while fetching user by username: " + e.getMessage(), e);
        }
    }

    public boolean isSubscriptionValid(int userId) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
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
        try (Connection conn = DBUtil.getConnection();
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


