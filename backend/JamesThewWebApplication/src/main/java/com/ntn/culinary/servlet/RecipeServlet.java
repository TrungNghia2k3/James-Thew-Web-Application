package com.ntn.culinary.servlet;

import com.google.gson.JsonSyntaxException;
import com.ntn.culinary.dao.AreaDao;
import com.ntn.culinary.dao.CategoryDao;
import com.ntn.culinary.dao.RecipeDao;
import com.ntn.culinary.dao.UserDao;
import com.ntn.culinary.dao.impl.AreaDaoImpl;
import com.ntn.culinary.dao.impl.CategoryDaoImpl;
import com.ntn.culinary.dao.impl.RecipeDaoImpl;
import com.ntn.culinary.dao.impl.UserDaoImpl;
import com.ntn.culinary.request.RecipeRequest;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.response.RecipePageResponse;
import com.ntn.culinary.response.RecipeResponse;
import com.ntn.culinary.service.RecipeService;
import com.ntn.culinary.utils.ValidationUtils;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
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
        this.recipeService = new RecipeService(recipeDao, categoryDao, areaDao, userDao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        // Get parameters from the request
        String idParam = req.getParameter("id");
        String pageParam = req.getParameter("page");
        String sizeParam = req.getParameter("size");

        try {
            if (idParam != null) {
                // If an ID is provided, fetch the recipe by ID
                handleGetById(resp, idParam);
                return;
            }

            // If no ID is provided, fetch the list of recipes
            handleGetAll(resp, pageParam, sizeParam);
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
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
                sendResponse(resp, new ApiResponse<>(400, "Invalid user ID"));
                return;
            }

            // Create RecipeRequest object
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

            // Validate input
            Map<String, String> errors = validateRecipeRequest(recipeRequest, imagePart);

            if (!errors.isEmpty()) {
                sendResponse(resp, new ApiResponse<>(400, "Validation failed", errors));
                return;
            }

            recipeService.addRecipe(recipeRequest, imagePart);

            sendResponse(resp, new ApiResponse<>(200, "Recipe added successfully"));

        } catch (JsonSyntaxException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid JSON data"));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Error adding recipe: " + e.getMessage()));
        }
    }

    private void handleGetById(HttpServletResponse resp, String idParam) {
        try {
            int id = Integer.parseInt(idParam);
            RecipeResponse recipe = recipeService.getFreeRecipeById(id);

            if (recipe != null) {
                sendResponse(resp, new ApiResponse<>(200, "Recipe found", recipe));
            } else {
                sendResponse(resp, new ApiResponse<>(404, "Recipe not found"));
            }
        } catch (NumberFormatException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid recipe ID"));
        }
    }

    private void handleGetAll(HttpServletResponse resp, String pageParam, String sizeParam) {

        // Parse pagination parameters
        int page = parseOrDefault(pageParam, 1);
        int size = parseOrDefault(sizeParam, 10);

        // Validate pagination parameters
        if (page < 1 || size < 1) {
            sendResponse(resp, new ApiResponse<>(400, "Page and size must be greater than 0"));
            return;
        }

        // Fetch recipes with pagination
        List<RecipeResponse> recipes = recipeService.getAllFreeRecipes(page, size);
        int totalItems = recipeService.countAllFreeRecipes();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        RecipePageResponse response = new RecipePageResponse(recipes, totalItems, page, totalPages);
        sendResponse(resp, new ApiResponse<>(200, "Free recipes fetched successfully", response));
    }

    private int parseOrDefault(String param, int defaultValue) {
        try {
            return param != null ? Integer.parseInt(param) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private Map<String, String> validateRecipeRequest(RecipeRequest recipeRequest, Part imagePart) {
        Map<String, String> errors = new HashMap<>();

        if (ValidationUtils.isNullOrEmpty(recipeRequest.getName())) {
            errors.put("name", "Name is required");
        }

        if (ValidationUtils.isNullOrEmpty(recipeRequest.getCategory())) {
            errors.put("category", "Category is required");
        }

        if (ValidationUtils.isNullOrEmpty(recipeRequest.getArea())) {
            errors.put("area", "Area is required");
        }

        if (ValidationUtils.isNullOrEmpty(recipeRequest.getInstructions())) {
            errors.put("instructions", "Instructions are required");
        }

        if (imagePart == null || imagePart.getSize() == 0) {
            errors.put("image", "Image is required");
        }

        if (ValidationUtils.isNullOrEmpty(recipeRequest.getIngredients())) {
            errors.put("ingredients", "Ingredients are required");
        }

        if (ValidationUtils.isNotExistId(recipeRequest.getRecipedBy())) {
            errors.put("recipedBy", "Reciped by is required");
        }

        if (ValidationUtils.isNullOrEmpty(recipeRequest.getPrepareTime())) {
            errors.put("prepareTime", "Prepare time is required");
        }

        if (ValidationUtils.isNullOrEmpty(recipeRequest.getCookingTime())) {
            errors.put("cookingTime", "Cooking time is required");
        }

        if (ValidationUtils.isNullOrEmpty(recipeRequest.getYield())) {
            errors.put("yield", "Yield is required");
        }

        if (ValidationUtils.isNullOrEmpty(recipeRequest.getShortDescription())) {
            errors.put("shortDescription", "Short description is required");
        }

        if (ValidationUtils.isNullOrEmpty(recipeRequest.getAccessType())) {
            errors.put("accessType", "Access type is required");
        }

        return errors;
    }
}
