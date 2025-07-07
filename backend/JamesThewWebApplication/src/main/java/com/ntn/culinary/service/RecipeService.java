package com.ntn.culinary.service;

import com.ntn.culinary.constant.AccessType;
import com.ntn.culinary.dao.AreaDao;
import com.ntn.culinary.dao.CategoryDao;
import com.ntn.culinary.dao.RecipeDao;
import com.ntn.culinary.dao.UserDao;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.model.DetailedInstructions;
import com.ntn.culinary.model.Recipe;
import com.ntn.culinary.request.RecipeRequest;
import com.ntn.culinary.utils.ImageUtils;
import com.ntn.culinary.response.RecipeResponse;
import com.ntn.culinary.utils.StringUtils;

import javax.servlet.http.Part;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class RecipeService {
    private final RecipeDao recipeDao;
    private final CategoryDao categoryDao;
    private final AreaDao areaDao;
    private final UserDao userDao;

    public RecipeService(RecipeDao recipeDao, CategoryDao categoryDao, AreaDao areaDao, UserDao userDao) {
        this.recipeDao = recipeDao;
        this.categoryDao = categoryDao;
        this.areaDao = areaDao;
        this.userDao = userDao;
    }

    public void addRecipe(RecipeRequest recipeRequest, Part imagePart) {
        validateRecipeRequest(recipeRequest);

        if (imagePart != null && imagePart.getSize() > 0) {
            String slug = ImageUtils.slugify(recipeRequest.getName());
            String fileName = ImageUtils.saveImage(imagePart, slug, "recipes");
            recipeRequest.setImage(fileName);
        }

        recipeDao.addRecipe(mapRequestToRecipe(recipeRequest));
    }

    public RecipeResponse getFreeRecipeById(int id)  {
        Recipe recipe = recipeDao.getFreeRecipeById(id);
        if (recipe == null) {
            return null;
        }
        return mapRecipeToResponse(recipe);
    }

    public List<RecipeResponse> getAllFreeRecipes(int page, int size){
        return recipeDao.getAllFreeRecipes(page, size).stream()
                .map(this::mapRecipeToResponse)
                .toList();
    }

    public List<RecipeResponse> searchAndFilterFreeRecipes(String keyword, String category, String area, int recipedBy, String accessType, int page, int size) {

        if (category != null) {
            category = StringUtils.capitalize(category);
        }

        if (area != null) {
            area = StringUtils.capitalize(area);
        }

        return recipeDao.searchAndFilterFreeRecipes(keyword, category, area, recipedBy, accessType, page, size).stream()
                .map(this::mapRecipeToResponse)
                .toList();
    }

    public int countSearchAndFilterFreeRecipes(String keyword, String category, String area, int recipedBy, String accessType) {

        if (category != null) {
            category = StringUtils.capitalize(category);
        }

        if (area != null) {
            area = StringUtils.capitalize(area);
        }

        return recipeDao.countSearchAndFilterFreeRecipes(keyword, category, area, recipedBy, accessType.toUpperCase());
    }

    public int countAllFreeRecipes() {
        return recipeDao.countAllFreeRecipes();
    }

    private void validateRecipeRequest(RecipeRequest recipeRequest){
        if (!categoryDao.existsByName(recipeRequest.getCategory())) {
            throw new NotFoundException("Category does not exist");
        }
        if (!areaDao.existsByName(recipeRequest.getArea())) {
            throw new NotFoundException("Area does not exist");
        }
        String accessType = recipeRequest.getAccessType();
        if (!String.valueOf(AccessType.FREE).equalsIgnoreCase(accessType) &&
                !String.valueOf(AccessType.PAID).equalsIgnoreCase(accessType)) {
            throw new NotFoundException("Invalid access type");
        }
        if (!userDao.existsById(recipeRequest.getRecipedBy())) {
            throw new NotFoundException("User does not exist");
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
        recipe.setPublishedOn(new Date(System.currentTimeMillis()));
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

        String detailedInstructionImageUrl = "http://localhost:8080/JamesThewWebApplication/api/images/instructions/";

        // Add image URL to each detailed instruction
        List<DetailedInstructions> updatedDetailedInstructions = recipe.getDetailedInstructions()
                .stream()
                .peek(instruction -> {
                    if (instruction.getImage() != null) {
                        instruction.setImage(detailedInstructionImageUrl + instruction.getImage());
                    }
                })
                .toList();

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
                recipe.getAccessType(),
                recipe.getComments(),
                recipe.getNutrition(),
                updatedDetailedInstructions
        );
    }
}