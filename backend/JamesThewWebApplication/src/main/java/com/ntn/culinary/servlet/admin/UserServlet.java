package com.ntn.culinary.servlet.admin;

import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.response.UserResponse;
import com.ntn.culinary.service.UserService;
import com.ntn.culinary.utils.ResponseUtils;
import com.ntn.culinary.utils.ValidationUtils;
import com.ntn.culinary.utils.CastUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/protected/admin/users")
public class UserServlet extends HttpServlet {
    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Lấy thông tin từ JwtFilter
        List<String> roles = CastUtils.toStringList(req.getAttribute("roles"));

        if (roles == null || !roles.contains("ADMIN")) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(403, "Access denied: ADMIN role required"));
            return;
        }

        String idParam = req.getParameter("id");

        try {
            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                UserResponse user = userService.getUserById(id);
                ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "User found", user));
            } else {
                List<UserResponse> users = userService.getAllUsers();
                ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "User list fetched", users));
            }
        } catch (Exception e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Lấy thông tin từ JwtFilter
        List<String> roles = CastUtils.toStringList(req.getAttribute("roles"));

        if (roles == null || !roles.contains("ADMIN")) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(403, "Access denied: ADMIN role required"));
            return;
        }
        try {

            int id = Integer.parseInt(req.getParameter("id"));

            userService.toggleUserStatus(id);
            ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "User status toggled successfully", null));
        } catch (Exception e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Lấy thông tin từ JwtFilter
        List<String> roles = CastUtils.toStringList(req.getAttribute("roles"));

        if (roles == null || !roles.contains("ADMIN")) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(403, "Access denied: ADMIN role required"));
            return;
        }

        // Logic to delete user data for admin
        try {

            int id = Integer.parseInt(req.getParameter("id"));

            if (ValidationUtils.isNotExistId(id)) {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(400, "User ID is required"));
                return;
            }

            userService.deleteUser(id);
            ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "User deleted successfully", null));
        } catch (Exception e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }
}
