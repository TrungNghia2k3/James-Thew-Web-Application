package com.ntn.culinary.servlet.admin;

import com.ntn.culinary.dao.CategoryDao;
import com.ntn.culinary.dao.impl.CategoryDaoImpl;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.service.CategoryService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.ntn.culinary.utils.CastUtils.toStringList;
import static com.ntn.culinary.utils.ResponseUtils.sendResponse;

@WebServlet("/api/protected/admin/categories")
public class CategoryServlet extends HttpServlet {

    private final CategoryService categoryService;

    public CategoryServlet() {
        CategoryDao categoryDao = new CategoryDaoImpl();
        this.categoryService = new CategoryService(categoryDao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        // Lấy thông tin từ JwtFilter
        List<String> roles = toStringList(req.getAttribute("roles"));

        if (roles == null || !roles.contains("ADMIN")) {
            sendResponse(resp, new ApiResponse<>(403, "Access denied: ADMIN role required"));
            return;
        }

        String idParam = req.getParameter("id");

        try {
            if (idParam != null) {
                handleGetById(idParam, resp);
            } else {
                handleGetAll(resp);
            }
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    private void handleGetById(String idParam, HttpServletResponse resp) {
        try {
            int id = Integer.parseInt(idParam);
            var category = categoryService.getCategoryById(id);

            if (category != null) {
                sendResponse(resp, new ApiResponse<>(200, "Category fetched successfully", category));
            } else {
                sendResponse(resp, new ApiResponse<>(404, "Category with ID " + id + " does not exist"));
            }
        } catch (NumberFormatException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid ID format"));
        }
    }

    private void handleGetAll(HttpServletResponse resp) {
        try {
            var categories = categoryService.getAllCategories();
            sendResponse(resp, new ApiResponse<>(200, "All categories fetched", categories));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Error fetching categories: " + e.getMessage()));
        }
    }
}
