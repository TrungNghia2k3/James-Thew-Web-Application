package com.ntn.culinary.servlet.staff;

import com.ntn.culinary.constant.PermissionType;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.utils.CastUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.ntn.culinary.utils.ResponseUtils.sendResponse;

@WebServlet("/api/protected/staff/secure-resource-2")
public class StaffOnlyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        // Lấy thông tin từ JwtFilter
        List<String> roles = CastUtils.toStringList(req.getAttribute("roles"));

        if (roles == null || !roles.contains("STAFF")) {
            sendResponse(resp, new ApiResponse<>(403, "Access denied: STAFF role required"));
            return;
        }

        List<String> permissions = CastUtils.toStringList(req.getAttribute("permissions"));

        if (permissions == null || !permissions.contains(String.valueOf(PermissionType.MANAGE_CONTESTS))) {
            sendResponse(resp, new ApiResponse<>(403, "Access denied: MANAGE_CONTESTS permission required"));
            return;
        }

        // Truy cập thành công
        sendResponse(resp, new ApiResponse<>(200, "OK"));
    }
}

