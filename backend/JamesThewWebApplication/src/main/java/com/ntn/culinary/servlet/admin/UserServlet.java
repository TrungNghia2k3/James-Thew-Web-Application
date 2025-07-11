package com.ntn.culinary.servlet.admin;

import com.ntn.culinary.dao.UserDao;
import com.ntn.culinary.dao.impl.UserDaoImpl;
import com.ntn.culinary.exception.ForbiddenException;
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

import static com.ntn.culinary.utils.CastUtils.toStringList;
import static com.ntn.culinary.utils.ResponseUtils.sendResponse;
import static com.ntn.culinary.utils.ValidationUtils.isNotExistId;

@WebServlet("/api/protected/admin/users")
public class UserServlet extends HttpServlet {
    private final UserService userService;

    public UserServlet() {
        UserDao userDao = new UserDaoImpl();
        this.userService = new UserService(userDao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String idParam = req.getParameter("id");

            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                UserResponse user = userService.getUserById(id);
                sendResponse(resp, new ApiResponse<>(200, "User found", user));
            }

            List<UserResponse> users = userService.getAllUsers();
            sendResponse(resp, new ApiResponse<>(200, "User list fetched", users));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            userService.toggleUserStatus(id);
            sendResponse(resp, new ApiResponse<>(200, "User status toggled successfully", null));
        } catch (NumberFormatException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid user ID format"));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // Logic to delete user data for admin
            int id = Integer.parseInt(req.getParameter("id"));
            if (isNotExistId(id)) throw new IllegalArgumentException("User ID is required and must exist");

            userService.deleteUser(id);
            sendResponse(resp, new ApiResponse<>(200, "User deleted successfully", null));
        } catch (NumberFormatException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid user ID format"));
        } catch (IllegalArgumentException e) {
            sendResponse(resp, new ApiResponse<>(400, e.getMessage()));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }
}
