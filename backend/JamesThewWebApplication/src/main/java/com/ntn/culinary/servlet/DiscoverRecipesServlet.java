package com.ntn.culinary.servlet;

import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.response.RecipePageResponse;
import com.ntn.culinary.response.RecipeResponse;
import com.ntn.culinary.service.RecipeService;
import com.ntn.culinary.utils.ResponseUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/discover/recipes")
public class DiscoverRecipesServlet extends HttpServlet {
    private final RecipeService recipeService = RecipeService.getInstance();

    // Thêm nhiều các bộ lọc và tìm kiếm cho các công thức nấu ăn

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Get parameters from the request
        String pageParam = req.getParameter("page");
        String sizeParam = req.getParameter("size");
        String keyword = req.getParameter("keyword");
        String category = req.getParameter("category");
        String area = req.getParameter("area");

        int recipedBy = 0;
        if (req.getParameter("recipedBy") != null) {
            recipedBy = Integer.parseInt(req.getParameter("recipedBy"));
        }
        try {
            handleGetListRecipe(resp, pageParam, sizeParam, keyword, category, area, recipedBy);
        } catch (Exception e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    private void handleGetListRecipe(HttpServletResponse resp,String pageParam, String sizeParam, String keyword, String category, String area, int recipedBy) throws IOException, SQLException {
        int page = parseOrDefault(pageParam, 1);
        int size = parseOrDefault(sizeParam, 10);

        String accessType = "FREE"; // Default access type

        List<RecipeResponse> recipes;
        int totalItems;

        recipes = recipeService.searchAndFilterFreeRecipes(keyword, category, area, recipedBy, accessType, page, size);
        totalItems = recipeService.countSearchAndFilterFreeRecipes(keyword, category, area, recipedBy, accessType);

        int totalPages = (int) Math.ceil((double) totalItems / size);
        RecipePageResponse response = new RecipePageResponse(recipes, totalItems, page, totalPages);

        ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "Free recipes fetched successfully", response));
    }

    private int parseOrDefault(String param, int defaultValue) {
        try {
            return param != null ? Integer.parseInt(param) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
