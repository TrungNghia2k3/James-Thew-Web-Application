package com.ntn.culinary.servlet.admin;

import com.ntn.culinary.dao.UserDao;
import com.ntn.culinary.dao.impl.UserDaoImpl;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.response.UserResponse;
import com.ntn.culinary.service.UserService;
import com.ntn.culinary.utils.CastUtils;
import com.ntn.culinary.utils.ValidationUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.ntn.culinary.utils.ResponseUtils.sendResponse;

@WebServlet("/api/protected/admin/users")
public class UserServlet extends HttpServlet {
    private final UserService userService;

    public UserServlet() {
        UserDao userDao = new UserDaoImpl();
        this.userService = new UserService(userDao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        // Lấy thông tin từ JwtFilter
        List<String> roles = CastUtils.toStringList(req.getAttribute("roles"));

        if (roles == null || !roles.contains("ADMIN")) {
            sendResponse(resp, new ApiResponse<>(403, "Access denied: ADMIN role required"));
            return;
        }

        String idParam = req.getParameter("id");

        try {
            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                UserResponse user = userService.getUserById(id);
                sendResponse(resp, new ApiResponse<>(200, "User found", user));
            } else {
                List<UserResponse> users = userService.getAllUsers();
                sendResponse(resp, new ApiResponse<>(200, "User list fetched", users));
            }
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        // Lấy thông tin từ JwtFilter
        List<String> roles = CastUtils.toStringList(req.getAttribute("roles"));

        if (roles == null || !roles.contains("ADMIN")) {
            sendResponse(resp, new ApiResponse<>(403, "Access denied: ADMIN role required"));
            return;
        }
        try {

            int id = Integer.parseInt(req.getParameter("id"));

            userService.toggleUserStatus(id);
            sendResponse(resp, new ApiResponse<>(200, "User status toggled successfully", null));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {

        // Lấy thông tin từ JwtFilter
        List<String> roles = CastUtils.toStringList(req.getAttribute("roles"));

        if (roles == null || !roles.contains("ADMIN")) {
            sendResponse(resp, new ApiResponse<>(403, "Access denied: ADMIN role required"));
            return;
        }

        // Logic to delete user data for admin
        try {

            int id = Integer.parseInt(req.getParameter("id"));

            if (ValidationUtils.isNotExistId(id)) {
                sendResponse(resp, new ApiResponse<>(400, "User ID is required"));
                return;
            }

            userService.deleteUser(id);
            sendResponse(resp, new ApiResponse<>(200, "User deleted successfully", null));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }
}
