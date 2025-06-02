package com.service;

import com.dao.UserDAO;
import com.model.User;
import com.request.UserRequest;
import com.response.UserResponse;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private final UserDAO userDao;

    public UserService() {
        this.userDao = new UserDAO();
    }

    public List<UserResponse> getAllUsers() throws SQLException {
        List<User> users = userDao.getAllUsers();
        return users.stream()
                .map(this::mapUserToResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(int id) throws SQLException {
        User user = userDao.getUserById(id);
        if (user == null) {
            return null;
        }
        return mapUserToResponse(user);
    }

    public void register(UserRequest request) throws SQLException {
        User user = mapRequestToUser(request);
        userDao.addUser(user);
    }

    public void editUser(UserRequest request) throws Exception {
        User existingUser = userDao.getUserById(request.getId());

        if (existingUser == null) {
            throw new Exception("User with ID " + request.getId() + " not found.");
        }

        userDao.editUser(existingUser);
    }

    public void deleteUser(int id) throws Exception {
        User existingUser = userDao.getUserById(id);

        if (existingUser == null) {
            throw new Exception("User with ID " + id + " not found.");
        }

        userDao.deleteUser(id);
    }

    public void toggleUserStatus(int id) throws Exception {

        User existingUser = userDao.getUserById(id);

        if (existingUser == null) {
            throw new Exception("User with ID " + id + " not found.");
        }

        userDao.toggleUserActiveStatus(id);
    }

    public boolean isSubscriptionValid(int id) throws SQLException {
        return userDao.isSubscriptionValid(id);
    }

    private User mapRequestToUser(UserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }

    private UserResponse mapUserToResponse(User user) {
        String imageUrl = "http://localhost:8080/JamesThewWebApplication/api/images/avatars/";

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhone(user.getPhone());
        response.setCreatedAt(user.getCreatedAt());
        response.setActive(user.isActive());
        response.setAvatar(user.getAvatar() != null ? imageUrl + user.getAvatar() : null);
        response.setLocation(user.getLocation());
        response.setSchool(user.getSchool());
        response.setHighlights(user.getHighlights());
        response.setExperience(user.getExperience());
        response.setEducation(user.getEducation());

        // Xử lý socialLinks thành List<String>
        String socialLinksStr = user.getSocialLinks();
        if (socialLinksStr != null && !socialLinksStr.trim().isEmpty()) {
            List<String> socialLinksList = Arrays.stream(socialLinksStr.split("\\|"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            response.setSocialLinks(socialLinksList);
        } else {
            response.setSocialLinks(Collections.emptyList());
        }

        // Thiết lập roles và permissions
        response.setRoles(user.getRoles());
        response.setPermissions(user.getPermissions());

        return response;
    }
}
