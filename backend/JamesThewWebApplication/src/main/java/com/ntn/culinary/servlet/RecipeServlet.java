package com.ntn.culinary.servlet;

import com.google.gson.JsonSyntaxException;
import com.ntn.culinary.dao.*;
import com.ntn.culinary.dao.impl.*;
import com.ntn.culinary.exception.ConflictException;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.exception.ValidationException;
import com.ntn.culinary.request.RecipeRequest;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.response.RecipePageResponse;
import com.ntn.culinary.response.RecipeResponse;
import com.ntn.culinary.service.RecipeService;
import com.ntn.culinary.utils.ValidationUtils;
import com.ntn.culinary.validator.RecipeRequestValidator;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ntn.culinary.utils.ResponseUtils.sendResponse;

@WebServlet("/api/recipes")
@MultipartConfig
public class RecipeServlet extends HttpServlet {
    private final RecipeService recipeService;

    public RecipeServlet() {
        RecipeDao recipeDao = new RecipeDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        AreaDao areaDao = new AreaDaoImpl();
        UserDao userDao = new UserDaoImpl();
        DetailedInstructionsDao detailedInstructionsDao = new DetailedInstructionsDaoImpl();
        CommentDao commentDao = new CommentDaoImpl();
        NutritionDao nutritionDao = new NutritionDaoImpl();
        this.recipeService = new RecipeService(recipeDao, categoryDao, areaDao, userDao, detailedInstructionsDao, commentDao, nutritionDao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // Get parameters from the request
            String idParam = req.getParameter("id");
            String pageParam = req.getParameter("page");
            String sizeParam = req.getParameter("size");
            String areaParam = req.getParameter("area");
            String categoryParam = req.getParameter("category");

            // Parse pagination parameters
            int page = parseOrDefault(pageParam, 1);
            int size = parseOrDefault(sizeParam, 10);

            // Validate pagination parameters
            if (page < 1 || size < 1) {
                throw new IllegalArgumentException("Page and Size cannot be less than 1");
            }

            if (idParam != null) {
                int id = Integer.parseInt(idParam);

                RecipeResponse recipe = recipeService.getFreeRecipeById(id);
                sendResponse(resp, new ApiResponse<>(200, "Recipe found", recipe));
            } else if (areaParam != null) {
                int areaId = Integer.parseInt(areaParam);
                List<RecipeResponse> recipes = recipeService.getAllFreeRecipesByAreaId(areaId, page, size);
                int totalItems = recipeService.countAllFreeRecipesByAreaId(areaId);
                int totalPages = (int) Math.ceil((double) totalItems / size);

                RecipePageResponse response = new RecipePageResponse(recipes, totalItems, page, totalPages);
                sendResponse(resp, new ApiResponse<>(200, "Recipes by area fetched successfully", response));
            } else if (categoryParam != null) {
                int categoryId = Integer.parseInt(categoryParam);
                List<RecipeResponse> recipes = recipeService.getAllFreeRecipesByCategoryId(categoryId, page, size);
                int totalItems = recipeService.countAllFreeRecipesByCategoryId(categoryId);
                int totalPages = (int) Math.ceil((double) totalItems / size);

                RecipePageResponse response = new RecipePageResponse(recipes, totalItems, page, totalPages);
                sendResponse(resp, new ApiResponse<>(200, "Recipes by category fetched successfully", response));
            } else {
                // Fetch recipes with pagination
                List<RecipeResponse> recipes = recipeService.getAllFreeRecipes(page, size);
                int totalItems = recipeService.countAllFreeRecipes();
                int totalPages = (int) Math.ceil((double) totalItems / size);
                RecipePageResponse response = new RecipePageResponse(recipes, totalItems, page, totalPages);
                sendResponse(resp, new ApiResponse<>(200, "Free recipes fetched successfully", response));
            }
        } catch (NumberFormatException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid recipe ID"));
        } catch (IllegalArgumentException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid pagination parameters: " + e.getMessage()));
        } catch (NotFoundException e) {
            sendResponse(resp, new ApiResponse<>(404, e.getMessage()));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    private int parseOrDefault(String param, int defaultValue) {
        return param != null ? Integer.parseInt(param) : defaultValue;
    }
}
