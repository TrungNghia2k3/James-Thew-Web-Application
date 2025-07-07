package com.ntn.culinary.servlet.admin;

import com.ntn.culinary.dao.RoleDao;
import com.ntn.culinary.dao.StaffPermissionsDao;
import com.ntn.culinary.dao.UserDao;
import com.ntn.culinary.dao.impl.RoleDaoImpl;
import com.ntn.culinary.dao.impl.StaffPermissionsDaoImpl;
import com.ntn.culinary.dao.impl.UserDaoImpl;
import com.ntn.culinary.request.StaffPermissionsRequest;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.service.StaffPermissionsService;
import com.ntn.culinary.utils.CastUtils;
import com.ntn.culinary.utils.GsonUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static com.ntn.culinary.utils.ResponseUtils.sendResponse;

@WebServlet("/api/protected/admin/staff-permissions")
public class StaffPermissionsServlet extends HttpServlet {
    private final StaffPermissionsService staffPermissionsService;

    public StaffPermissionsServlet() {
        StaffPermissionsDao staffPermissionsDao = new StaffPermissionsDaoImpl();
        UserDao userDao = new UserDaoImpl();
        RoleDao roleDao = new RoleDaoImpl();
        this.staffPermissionsService = new StaffPermissionsService(staffPermissionsDao, userDao, roleDao);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // Lấy thông tin từ JwtFilter
            List<String> roles = CastUtils.toStringList(req.getAttribute("roles"));

            if (roles == null || !roles.contains("ADMIN")) {
                sendResponse(resp, new ApiResponse<>(403, "Access denied: ADMIN role required"));
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
                sendResponse(resp, new ApiResponse<>(400, "Invalid request payload"));
                return;
            }

            // Parse JSON sting to StaffPermissionsRequest object by Gson
            StaffPermissionsRequest staffPermissionsRequest = GsonUtils.fromJson(sb.toString(), StaffPermissionsRequest.class);

            int userId = staffPermissionsRequest.getUserId();
            int roleId = staffPermissionsRequest.getRoleId();

            // Validate userId and roleId
            if (userId <= 0 || roleId <= 0) {
                sendResponse(resp, new ApiResponse<>(400, "Invalid userId or roleId"));
            }

            staffPermissionsService.assignPermissionToStaff(userId, roleId);
            sendResponse(resp, new ApiResponse<>(200, "Role assigned successfully"));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Database error: " + e.getMessage()));
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

        try {
            // Read request body and build JSON string
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                sendResponse(resp, new ApiResponse<>(400, "Invalid request payload"));
                return;
            }

            // Parse JSON sting to StaffPermissionsRequest object by Gson
            StaffPermissionsRequest staffPermissionsRequest = GsonUtils.fromJson(sb.toString(), StaffPermissionsRequest.class);

            int userId = staffPermissionsRequest.getUserId();
            int roleId = staffPermissionsRequest.getRoleId();

            // Validate userId and roleId
            if (userId <= 0 || roleId <= 0) {
                sendResponse(resp, new ApiResponse<>(400, "Invalid userId or roleId"));
            }

            staffPermissionsService.removePermissionFromStaff(userId, roleId);
            sendResponse(resp, new ApiResponse<>(200, "Role removed successfully"));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Database error: " + e.getMessage()));
        }
    }
}
