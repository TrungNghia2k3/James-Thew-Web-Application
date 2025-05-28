package com.servlet;

import com.google.gson.JsonSyntaxException;
import com.request.RecipeRequest;
import com.response.ApiResponse;
import com.response.RecipePageResponse;
import com.response.RecipeResponse;
import com.service.RecipeService;
import com.util.ResponseUtil;
import com.util.ValidationUtil;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/recipes/free")
@MultipartConfig
public class FreeRecipeServlet extends HttpServlet {
    private final RecipeService recipeService = new RecipeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        // Get parameters from the request
        String idParam = req.getParameter("id");
        String pageParam = req.getParameter("page");
        String sizeParam = req.getParameter("size");
        String keyword = req.getParameter("keyword");
        String category = req.getParameter("category");

        try {
            if (idParam != null) {
                // If an ID is provided, fetch the recipe by ID
                handleGetById(resp, idParam);
                return;
            }

            // If no ID is provided, fetch the list of recipes
            handleGetList(resp, pageParam, sizeParam, keyword, category);
        } catch (Exception e) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            req.setCharacterEncoding("UTF-8");

            // Lấy dữ liệu form
            String name = req.getParameter("name");
            String category = req.getParameter("category");
            String area = req.getParameter("area");
            String instructions = req.getParameter("instructions");
            Part imagePart = req.getPart("image");
            String ingredients = req.getParameter("ingredients");
            String publishedOn = req.getParameter("publishedOn");
            String prepareTime = req.getParameter("prepareTime");
            String cookingTime = req.getParameter("cookingTime");
            String yield = req.getParameter("yield");
            String shortDescription = req.getParameter("shortDescription");
            String accessType = req.getParameter("accessType");

            int recipedBy;
            try {
                recipedBy = Integer.parseInt(req.getParameter("recipedBy"));
            } catch (NumberFormatException e) {
                ResponseUtil.sendResponse(resp, new ApiResponse<>(400, "Invalid user ID"));
                return;
            }

            // Validate input
            Map<String, String> errors = new HashMap<>();

            if (ValidationUtil.isNullOrEmpty(name)) {
                errors.put("name", "Name is required");
            }

            if (ValidationUtil.isNullOrEmpty(category)) {
                errors.put("category", "Category is required");
            }

            if (ValidationUtil.isNullOrEmpty(area)) {
                errors.put("area", "Area is required");
            }

            if (ValidationUtil.isNullOrEmpty(instructions)) {
                errors.put("instructions", "Instructions are required");
            }

            if (imagePart == null || imagePart.getSize() == 0) {
                errors.put("image", "Image is required");
            }

            if (ValidationUtil.isNullOrEmpty(ingredients)) {
                errors.put("ingredients", "Ingredients are required");
            }

            if (ValidationUtil.isNullOrEmpty(publishedOn)) {
                errors.put("publishedOn", "Published date is required");
            }

            if (ValidationUtil.isNotExistId(recipedBy)) {
                errors.put("recipedBy", "Reciped by is required");
            }

            if (ValidationUtil.isNullOrEmpty(prepareTime)) {
                errors.put("prepareTime", "Prepare time is required");
            }

            if (ValidationUtil.isNullOrEmpty(cookingTime)) {
                errors.put("cookingTime", "Cooking time is required");
            }

            if (ValidationUtil.isNullOrEmpty(yield)) {
                errors.put("yield", "Yield is required");
            }

            if (ValidationUtil.isNullOrEmpty(shortDescription)) {
                errors.put("shortDescription", "Short description is required");
            }

            if (ValidationUtil.isNullOrEmpty(accessType)) {
                errors.put("accessType", "Access type is required");
            }

            if (!errors.isEmpty()) {
                ResponseUtil.sendResponse(resp, new ApiResponse<>(400, "Validation failed", errors));
                return;
            }

            RecipeRequest recipeRequest = new RecipeRequest();
            recipeRequest.setName(name);
            recipeRequest.setCategory(category);
            recipeRequest.setArea(area);
            recipeRequest.setInstructions(instructions);
            recipeRequest.setIngredients(ingredients);
            recipeRequest.setPublishedOn(new SimpleDateFormat("yyyy-MM-dd").parse(publishedOn));
            recipeRequest.setRecipedBy(recipedBy);
            recipeRequest.setPrepareTime(prepareTime);
            recipeRequest.setCookingTime(cookingTime);
            recipeRequest.setYield(yield);
            recipeRequest.setShortDescription(shortDescription);
            recipeRequest.setAccessType(accessType);

            recipeService.addRecipe(recipeRequest, imagePart, getServletContext());

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            ResponseUtil.sendResponse(resp, new ApiResponse<>(200, "Recipe added successfully"));

        } catch (JsonSyntaxException e) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(400, "Invalid JSON data"));
        } catch (Exception e) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(500, "Error adding recipe: " + e.getMessage()));
        }
    }

    private void handleGetById(HttpServletResponse resp, String idParam) throws IOException {
        try {
            int id = Integer.parseInt(idParam);
            RecipeResponse recipe = recipeService.getFreeRecipeById(id);

            if (recipe != null) {
                ResponseUtil.sendResponse(resp, new ApiResponse<>(200, "Recipe found", recipe));
            } else {
                ResponseUtil.sendResponse(resp, new ApiResponse<>(404, "Recipe not found"));
            }
        } catch (NumberFormatException | SQLException e) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(400, "Invalid recipe ID"));
        }
    }

    private void handleGetList(HttpServletResponse resp, String pageParam, String sizeParam, String keyword, String category) throws IOException, SQLException {
        int page = parseOrDefault(pageParam, 1);
        int size = parseOrDefault(sizeParam, 10);

        List<RecipeResponse> recipes;
        int totalItems;

        boolean hasSearch = (keyword != null && !keyword.isEmpty()) || (category != null && !category.isEmpty());

        if (hasSearch) {
            recipes = recipeService.searchAndFilterFreeRecipes(keyword, category, page, size);
            totalItems = recipeService.countSearchAndFilterFreeRecipes(keyword, category);
        } else {
            recipes = recipeService.getAllFreeRecipes(page, size);
            totalItems = recipeService.countAllFreeRecipes();
        }

        int totalPages = (int) Math.ceil((double) totalItems / size);
        RecipePageResponse response = new RecipePageResponse(recipes, totalItems, page, totalPages);

        ResponseUtil.sendResponse(resp, new ApiResponse<>(200, "Free recipes fetched successfully", response));
    }

    private int parseOrDefault(String param, int defaultValue) {
        try {
            return param != null ? Integer.parseInt(param) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
