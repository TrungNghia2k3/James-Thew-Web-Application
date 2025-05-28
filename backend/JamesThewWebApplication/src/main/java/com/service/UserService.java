package com.service;

import com.dao.UserDAO;
import com.exception.UserNotFoundException;
import com.request.UserRequest;
import com.response.UserResponse;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDAO userDao;

    public UserService() {
        this.userDao = new UserDAO();
    }

    public List<UserResponse> getAllUsers() throws SQLException {
        return userDao.getAllUsers();
    }

    public UserResponse getUserById(int id) throws UserNotFoundException {
        return userDao.getUserById(id);
    }

    public void register(UserRequest request) throws SQLException {
        userDao.addUser(request);
    }

//    public void editUser(UserRequest request) throws UserNotFoundException, SQLException {
//        userDao.editUser(request);
//    }

    public void deleteUser(int id) throws UserNotFoundException, SQLException {
        userDao.deleteUser(id);
    }

    public boolean isSubscriptionValid(int id) throws SQLException {
        return userDao.isSubscriptionValid(id);
    }
}
