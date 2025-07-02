package com.ntn.culinary.servlet.admin;

import com.ntn.culinary.request.UserRolesRequest;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.service.UserRolesService;
import com.ntn.culinary.utils.CastUtils;
import com.ntn.culinary.utils.GsonUtils;
import com.ntn.culinary.utils.ResponseUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/protected/admin/user-roles")
public class UserRolesServlet extends HttpServlet {
    private final UserRolesService userRolesService = UserRolesService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {

            // Lấy thông tin từ JwtFilter
            List<String> roles = CastUtils.toStringList(req.getAttribute("roles"));

            if (roles == null || !roles.contains("ADMIN")) {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(403, "Access denied: ADMIN role required"));
                return;
            }

            // Read request body and build JSON string
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(400, "Invalid request payload"));
                return;
            }

            // Parse JSON sting to UserRolesRequest object by Gson
            UserRolesRequest userRolesRequest = GsonUtils.fromJson(sb.toString(), UserRolesRequest.class);

            int userId = userRolesRequest.getUserId();
            int roleId = userRolesRequest.getRoleId();

            // Validate userId and roleId
            if (userId <= 0 || roleId <= 0) {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(400, "Invalid userId or roleId"));
            }

            userRolesService.assignRoleToUser(userId, roleId);
            ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "Role assigned successfully"));
        } catch (Exception e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Database error: " + e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {

            // Lấy thông tin từ JwtFilter
            List<String> roles = CastUtils.toStringList(req.getAttribute("roles"));

            if (roles == null || !roles.contains("ADMIN")) {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(403, "Access denied: ADMIN role required"));
                return;
            }

            // Read request body and build JSON string
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(400, "Invalid request payload"));
                return;
            }

            // Parse JSON sting to UserRolesRequest object by Gson
            UserRolesRequest userRolesRequest = GsonUtils.fromJson(sb.toString(), UserRolesRequest.class);

            int userId = userRolesRequest.getUserId();
            int roleId = userRolesRequest.getRoleId();

            // Validate userId and roleId
            if (userId <= 0 || roleId <= 0) {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(400, "Invalid userId or roleId"));
            }

            userRolesService.removeRoleFromUser(userId, roleId);
            ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "Role removed successfully"));
        } catch (Exception e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Database error: " + e.getMessage()));
        }
    }
}

