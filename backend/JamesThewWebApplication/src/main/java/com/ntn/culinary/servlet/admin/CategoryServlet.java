package com.ntn.culinary.servlet.admin;

import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.service.CategoryService;
import com.ntn.culinary.utils.CastUtils;
import com.ntn.culinary.utils.ResponseUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/protected/admin/categories")
public class CategoryServlet extends HttpServlet {

    private final CategoryService categoryService = CategoryService.getInstance();

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

    private void handleGetById(String idParam, HttpServletResponse resp) throws IOException, SQLException {
        try {
            int id = Integer.parseInt(idParam);
            var category = categoryService.getCategoryById(id);

            if (category != null) {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "Category fetched successfully", category));
            } else {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(404, "Category with ID " + id + " does not exist"));
            }
        } catch (NumberFormatException e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(400, "Invalid ID format"));
        }
    }

    private void handleGetAll(HttpServletResponse resp) throws IOException {
        try {
            var categories = categoryService.getAllCategories();
            ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "All categories fetched", categories));
        } catch (Exception e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Error fetching categories: " + e.getMessage()));
        }
    }
}
