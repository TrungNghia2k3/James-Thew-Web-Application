package com.service;

import com.constant.AccessType;
import com.dao.AreaDAO;
import com.dao.CategoryDAO;
import com.dao.RecipeDAO;
import com.dao.UserDAO;
import com.model.Recipe;
import com.request.RecipeRequest;
import com.response.RecipeResponse;
import com.util.ImageUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import java.sql.SQLException;
import java.util.List;

public class RecipeService {
    private final RecipeDAO recipeDao;
    private final CategoryDAO categoryDao;
    private final AreaDAO areaDao;
    private final UserDAO userDAO;

    // Default constructor for Servlet (no DI)
    public RecipeService() {
        this.recipeDao = new RecipeDAO();
        this.categoryDao = new CategoryDAO();
        this.areaDao = new AreaDAO();
        this.userDAO = new UserDAO();
    }

    // Constructor for unit testing (inject mock DAOs)
    public RecipeService(RecipeDAO recipeDao, CategoryDAO categoryDao, AreaDAO areaDao, UserDAO userDAO) {
        this.recipeDao = recipeDao;
        this.categoryDao = categoryDao;
        this.areaDao = areaDao;
        this.userDAO = userDAO;
    }

    public void addRecipe(RecipeRequest recipeRequest, Part imagePart, ServletContext servletContext) throws Exception {
        validateRecipeRequest(recipeRequest);

        if (imagePart != null && imagePart.getSize() > 0) {
            String slug = ImageUtil.slugify(recipeRequest.getName());
            String fileName = ImageUtil.saveImage(imagePart, slug);
            recipeRequest.setImage(fileName);
        }

        Recipe recipe = mapRequestToRecipe(recipeRequest);
        recipeDao.addRecipe(recipe);
    }

    public RecipeResponse getFreeRecipeById(int id) throws SQLException {
        Recipe recipe = recipeDao.getFreeRecipeById(id);

        if (recipe == null) {
            return null;
        }

        return mapRecipeToResponse(recipe);
    }


    public List<RecipeResponse> getAllFreeRecipes(int page, int size) throws SQLException {
        return recipeDao.getAllFreeRecipes(page, size).stream()
                .map(this::mapRecipeToResponse)
                .toList();
    }

    public List<RecipeResponse> searchAndFilterFreeRecipes(String keyword, String category, int page, int size) throws SQLException {
        return recipeDao.searchAndFilterFreeRecipes(keyword, category, page, size).stream()
                .map(this::mapRecipeToResponse)
                .toList();
    }

    public int countSearchAndFilterFreeRecipes(String keyword, String category) throws SQLException {
        return recipeDao.countSearchAndFilterFreeRecipes(keyword, category);
    }

    public int countAllFreeRecipes() throws SQLException {
        return recipeDao.countAllFreeRecipes();
    }

    private void validateRecipeRequest(RecipeRequest recipeRequest) throws SQLException {
        if (!categoryDao.existsByName(recipeRequest.getCategory())) {
            throw new SQLException("Category does not exist");
        }
        if (!areaDao.existsByName(recipeRequest.getArea())) {
            throw new SQLException("Area does not exist");
        }
        String accessType = recipeRequest.getAccessType();
        if (!String.valueOf(AccessType.FREE).equalsIgnoreCase(accessType) &&
                !String.valueOf(AccessType.PAID).equalsIgnoreCase(accessType)) {
            throw new SQLException("Invalid access type");
        }
        if (!userDAO.existsById(recipeRequest.getRecipedBy())) {
            throw new SQLException("User does not exist");
        }
    }

    private Recipe mapRequestToRecipe(RecipeRequest request) {
        Recipe recipe = new Recipe();
        recipe.setName(request.getName());
        recipe.setCategory(request.getCategory());
        recipe.setArea(request.getArea());
        recipe.setInstructions(request.getInstructions());
        recipe.setImage(request.getImage());
        recipe.setIngredients(request.getIngredients());
        recipe.setPublishedOn(request.getPublishedOn());
        recipe.setRecipedBy(request.getRecipedBy());
        recipe.setPrepareTime(request.getPrepareTime());
        recipe.setCookingTime(request.getCookingTime());
        recipe.setYield(request.getYield());
        recipe.setShortDescription(request.getShortDescription());
        recipe.setAccessType(request.getAccessType());
        return recipe;
    }

    private RecipeResponse mapRecipeToResponse(Recipe recipe) {
        String imageUrl = "http://localhost:8080/JamesThewWebApplication/api/images/recipes/" + recipe.getImage();
        return new RecipeResponse(
                recipe.getId(),
                recipe.getName(),
                recipe.getCategory(),
                recipe.getArea(),
                recipe.getInstructions(),
                imageUrl,
                recipe.getIngredients(),
                recipe.getPublishedOn(),
                recipe.getRecipedBy(),
                recipe.getPrepareTime(),
                recipe.getCookingTime(),
                recipe.getYield(),
                recipe.getShortDescription(),
                recipe.getAccessType()
        );
    }
}