package com.ntn.culinary.service;

import com.ntn.culinary.dao.UserDAO;
import com.ntn.culinary.model.User;
import com.ntn.culinary.request.RegisterRequest;
import com.ntn.culinary.request.UserRequest;
import com.ntn.culinary.response.UserResponse;
import com.ntn.culinary.util.ImageUtil;

import javax.servlet.http.Part;
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

    public void register(RegisterRequest request) throws SQLException {
        User user = mapRequestToRegisterRequest(request);
        userDao.addUser(user);
    }

    public void editGeneralUser(UserRequest request, Part avatar) throws Exception {
        User existingUser = userDao.getUserById(request.getId());

        if (existingUser == null) {
            throw new Exception("User with ID " + request.getId() + " not found.");
        }

        if (avatar != null && avatar.getSize() > 0) {
            // Xóa ảnh cũ nếu có
            if (existingUser.getAvatar() != null) {
                ImageUtil.deleteImage(existingUser.getAvatar(), "avatars");
            }

            // Tạo slug từ tên người dùng
            String slug = ImageUtil.slugify(request.getFirstName() + " " + request.getLastName());

            // Lưu ảnh và cập nhật tên file
            String fileName = ImageUtil.saveImage(avatar, slug, "avatars");
            request.setAvatar(fileName);
        }

        userDao.editGeneralUser(mapRequestToUser(request));
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
        user.setId(request.getId());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setAvatar(request.getAvatar());
        user.setLocation(request.getLocation());
        user.setSchool(request.getSchool());
        user.setHighlights(request.getHighlights());
        user.setExperience(request.getExperience());
        user.setEducation(request.getEducation());
        user.setSocialLinks(String.join("|", request.getSocialLinks()));
        return user;
    }

    private User mapRequestToRegisterRequest(RegisterRequest request) {
        User user = new User();

        user.setUsername(request.getUsername());
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
