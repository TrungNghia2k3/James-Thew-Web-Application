package com.dao;

import com.dto.request.UserRequest;
import com.dto.response.UserResponse;
import com.exception.UserNotFoundException;
import com.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class UserDao {
    private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_USER_QUERY = "INSERT INTO users (name, email) VALUES (?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET name = ?, email = ? WHERE id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";

    public List<UserResponse> getAllUsers() throws SQLException {
        return executeSQLQuery(SELECT_ALL_USERS_QUERY, null);
    }

    public UserResponse getUserById(int id) throws UserNotFoundException {
        try {
            List<UserResponse> users = executeSQLQuery(SELECT_USER_BY_ID_QUERY,
                    preparedStatement -> {
                        try {
                            preparedStatement.setInt(1, id);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
            if (users.isEmpty()) {
                throw new UserNotFoundException("User with id " + id + " not found.");
            } else {
                return users.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserNotFoundException("Error while fetching user with id " + id + ".");
        }
    }

    public void addUser(UserRequest user) {
        try {
            executeUpdate(INSERT_USER_QUERY,
                    preparedStatement -> {
                        try {
                            preparedStatement.setString(1, user.getName());
                            preparedStatement.setString(2, user.getEmail());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editUser(UserRequest user) throws UserNotFoundException {
        if (userExists(user.getId())) {
            try {
                executeUpdate(UPDATE_USER_QUERY,
                        preparedStatement -> {
                            try {
                                preparedStatement.setString(1, user.getName());
                                preparedStatement.setString(2, user.getEmail());
                                preparedStatement.setInt(3, user.getId());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            throw new UserNotFoundException("User with id " + user.getId() + " not found.");
        }
    }

    public void deleteUser(int id) throws UserNotFoundException {
        if (userExists(id)) {
            try {
                executeUpdate(DELETE_USER_QUERY,
                        preparedStatement -> {
                            try {
                                preparedStatement.setInt(1, id);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            throw new UserNotFoundException("User with id " + id + " not found.");
        }
    }

    private List<UserResponse> executeSQLQuery(String query, Consumer<PreparedStatement> consumer) throws SQLException {
        List<UserResponse> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (consumer != null) {
                consumer.accept(stmt);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToUser(rs));
                }
            }
        }
        return list;
    }

    private boolean userExists(int id) {
        try {
            getUserById(id);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }

    private void executeUpdate(String query, Consumer<PreparedStatement> consumer) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            consumer.accept(stmt);
            stmt.executeUpdate();
        }
    }

    private UserResponse mapResultSetToUser(ResultSet rs) throws SQLException {
        UserResponse u = new UserResponse();
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        return u;
    }
}

