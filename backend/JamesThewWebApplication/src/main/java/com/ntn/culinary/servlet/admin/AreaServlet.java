package com.ntn.culinary.servlet.admin;

import com.ntn.culinary.request.AreaRequest;
import com.ntn.culinary.service.AreaService;
import com.ntn.culinary.utils.CastUtils;
import com.ntn.culinary.utils.GsonUtils;
import com.ntn.culinary.utils.ValidationUtils;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.utils.ResponseUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/protected/admin/areas")
public class AreaServlet extends HttpServlet {

    private final AreaService areaService = AreaService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Lấy thông tin từ JwtFilter
        List<String> roles = CastUtils.toStringList(req.getAttribute("roles"));

        if (roles == null || !roles.contains("ADMIN")) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(403, "Access denied: ADMIN role required"));
            return;
        }

        // Lấy thông tin từ JwtFilter
        String idParam = req.getParameter("id");

        try {
            if (idParam != null) {
                handleGetById(idParam, resp);
            } else {
                handleGetAll(resp);
            }
        } catch (SQLException e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Database error: " + e.getMessage()));
        } catch (Exception e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Lấy thông tin từ JwtFilter
        List<String> roles = CastUtils.toStringList(req.getAttribute("roles"));

        if (roles == null || !roles.contains("ADMIN")) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(403, "Access denied: ADMIN role required"));
            return;
        }

        // Read JSON payload
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

        // Parse JSON
        AreaRequest areaRequest = GsonUtils.fromJson(sb.toString(), AreaRequest.class);

        // Validate input
        if (ValidationUtils.isNullOrEmpty(areaRequest.getName())) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(400, "Area name is required"));
            return;
        }

        try {
            areaService.addArea(areaRequest.getName());
            ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "Area added successfully"));
        } catch (Exception e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    private void handleGetById(String idParam, HttpServletResponse resp) throws SQLException, IOException {
        try {
            int id = Integer.parseInt(idParam);
            var area = areaService.getAreaById(id);

            if (area != null) {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "Area fetched successfully", area));
            } else {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(404, "Area with ID " + id + " does not exist"));
            }
        } catch (NumberFormatException e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(400, "Invalid ID format"));
        }
    }

    private void handleGetAll(HttpServletResponse resp) throws IOException {
        try {
            var areas = areaService.getAllAreas();
            ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "All areas fetched", areas));
        } catch (SQLException e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Database error: " + e.getMessage()));
        }
    }
}
