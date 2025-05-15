package com.service;

import com.dao.UserDao;
import com.request.UserRequest;
import com.response.UserResponse;
import com.exception.UserNotFoundException;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDao userDao = new UserDao();

    public List<UserResponse> getAllUsers() throws SQLException {
        return userDao.getAllUsers();
    }

    public UserResponse getUserById(int id) throws UserNotFoundException {
        return userDao.getUserById(id);
    }

    public void addUser(UserRequest request) throws SQLException {
        userDao.addUser(request);
    }

    public void editUser(UserRequest request) throws UserNotFoundException, SQLException {
        userDao.editUser(request);
    }

    public void deleteUser(int id) throws UserNotFoundException, SQLException {
        userDao.deleteUser(id);
    }

    public boolean isSubscriptionValid(int id) throws SQLException {
        return userDao.isSubscriptionValid(id);
    }
}
