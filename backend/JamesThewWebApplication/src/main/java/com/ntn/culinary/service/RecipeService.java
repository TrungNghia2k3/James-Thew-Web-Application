package com.ntn.culinary.service;

import com.ntn.culinary.constant.AccessType;
import com.ntn.culinary.dao.*;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.model.DetailedInstructions;
import com.ntn.culinary.model.Recipe;
import com.ntn.culinary.request.RecipeRequest;
import com.ntn.culinary.response.RecipeResponse;
import com.ntn.culinary.utils.ImageUtils;

import javax.servlet.http.Part;
import java.sql.Date;
import java.util.List;

import static com.ntn.culinary.utils.StringUtils.capitalize;

public class RecipeService {
    private final RecipeDao recipeDao;
    private final CategoryDao categoryDao;
    private final AreaDao areaDao;
    private final UserDao userDao;
    private final DetailedInstructionsDao detailedInstructionsDao;
    private final CommentDao commentDao;
    private final NutritionDao nutritionDao;

    public RecipeService(RecipeDao recipeDao, CategoryDao categoryDao, AreaDao areaDao, UserDao userDao,
                         DetailedInstructionsDao detailedInstructionsDao, CommentDao commentDao, NutritionDao nutritionDao) {
        this.recipeDao = recipeDao;
        this.categoryDao = categoryDao;
        this.areaDao = areaDao;
        this.userDao = userDao;
        this.detailedInstructionsDao = detailedInstructionsDao;
        this.commentDao = commentDao;
        this.nutritionDao = nutritionDao;
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

    public void updateRecipe(RecipeRequest recipeRequest, Part imagePart) {
        validateRecipeRequest(recipeRequest);

        Recipe existingRecipe = recipeDao.getRecipeById(recipeRequest.getId());
        if (existingRecipe == null) {
            throw new NotFoundException("Recipe with ID " + recipeRequest.getId() + " does not exist.");
        }

        if (imagePart != null && imagePart.getSize() > 0) {
            // Delete old image if it exists
            if (existingRecipe.getImage() != null) {
                ImageUtils.deleteImage(existingRecipe.getImage(), "recipes");
            }
            String slug = ImageUtils.slugify(recipeRequest.getName());
            String fileName = ImageUtils.saveImage(imagePart, slug, "recipes");
            recipeRequest.setImage(fileName);
        } else {
            recipeRequest.setImage(existingRecipe.getImage());
        }

        recipeDao.updateRecipe(mapRequestToRecipe(recipeRequest));
    }

    public void deleteRecipe(int id) {
        Recipe existingRecipe = recipeDao.getRecipeById(id);
        if (existingRecipe == null) {
            throw new NotFoundException("Recipe with ID " + id + " does not exist.");
        }

        // Delete image if it exists
        if (existingRecipe.getImage() != null) {
            ImageUtils.deleteImage(existingRecipe.getImage(), "recipes");
        }

        recipeDao.deleteRecipe(id);
    }

    public RecipeResponse getFreeRecipeById(int id) {
        Recipe recipe = recipeDao.getFreeRecipeById(id);
        if (recipe == null) {
            throw new NotFoundException("Recipe not found");
        }
        return mapRecipeToResponse(recipe);
    }

    public RecipeResponse getRecipeById(int id) {
        if (recipeDao.existsById(id)) {
            Recipe recipe = recipeDao.getRecipeById(id);
            return mapRecipeToResponse(recipe);
        } else {
            throw new NotFoundException("Recipe with ID " + id + " does not exist.");
        }

    }

    public List<RecipeResponse> getAllFreeRecipes(int page, int size) {
        return recipeDao.getAllFreeRecipes(page, size).stream()
                .map(this::mapRecipeToResponse)
                .toList();
    }

    public List<RecipeResponse> getAllRecipes(int page, int size) {
        return recipeDao.getAllRecipes(page, size).stream()
                .map(this::mapRecipeToResponse)
                .toList();
    }

    public List<RecipeResponse> searchAndFilterFreeRecipes(String keyword, String category, String area, int recipedBy, String accessType, int page, int size) {

        if (category != null) {
            category = capitalize(category);
        }

        if (area != null) {
            area = capitalize(area);
        }

        return recipeDao.searchAndFilterFreeRecipes(keyword, category, area, recipedBy, accessType, page, size).stream()
                .map(this::mapRecipeToResponse)
                .toList();
    }

    public List<RecipeResponse> searchAndFilterRecipes(String keyword, String category, String area, int recipedBy, int page, int size) {

        if (category != null) {
            category = capitalize(category);
        }

        if (area != null) {
            area = capitalize(area);
        }

        return recipeDao.searchAndFilterRecipes(keyword, category, area, recipedBy, page, size).stream()
                .map(this::mapRecipeToResponse)
                .toList();
    }

    public List<RecipeResponse> getAllRecipesByUserId(int userId, int page, int size) {
        return recipeDao.getAllRecipesByUserId(userId, page, size).stream()
                .map(this::mapRecipeToResponse)
                .toList();
    }

    public List<RecipeResponse> getAllFreeRecipesByCategoryId(int categoryId, int page, int size) {
        return recipeDao.getAllFreeRecipesByCategoryId(categoryId, page, size).stream()
                .map(this::mapRecipeToResponse)
                .toList();
    }

    public List<RecipeResponse> getAllRecipesByCategoryId(int categoryId, int page, int size) {
        return recipeDao.getAllRecipesByCategoryId(categoryId, page, size).stream()
                .map(this::mapRecipeToResponse)
                .toList();
    }

    public List<RecipeResponse> getAllFreeRecipesByAreaId(int areaId, int page, int size) {
        return recipeDao.getAllFreeRecipesByAreaId(areaId, page, size).stream()
                .map(this::mapRecipeToResponse)
                .toList();
    }

    public List<RecipeResponse> getAllRecipesByAreaId(int areaId, int page, int size) {
        return recipeDao.getAllRecipesByAreaId(areaId, page, size).stream()
                .map(this::mapRecipeToResponse)
                .toList();
    }

    public int countSearchAndFilterFreeRecipes(String keyword, String category, String area, int recipedBy, String accessType) {

        if (category != null) {
            category = capitalize(category);
        }

        if (area != null) {
            area = capitalize(area);
        }

        return recipeDao.countSearchAndFilterFreeRecipes(keyword, category, area, recipedBy, accessType.toUpperCase());
    }

    public int countSearchAndFilterRecipes(String keyword, String category, String area, int recipedBy) {

        if (category != null) {
            category = capitalize(category);
        }

        if (area != null) {
            area = capitalize(area);
        }

        return recipeDao.countSearchAndFilterRecipes(keyword, category, area, recipedBy);
    }

    public int countAllFreeRecipes() {
        return recipeDao.countAllFreeRecipes();
    }

    public int countAllRecipes() {
        return recipeDao.countAllRecipes();
    }

    public int countAllRecipesByUserId(int userId) {
        return recipeDao.countAllRecipesByUserId(userId);
    }

    public int countAllFreeRecipesByCategoryId(int categoryId) {
        return recipeDao.countAllFreeRecipesByCategoryId(categoryId);
    }

    public int countAllRecipesByCategoryId(int categoryId) {
        return recipeDao.countAllRecipesByCategoryId(categoryId);
    }

    public int countAllFreeRecipesByAreaId(int areaId) {
        return recipeDao.countAllFreeRecipesByAreaId(areaId);
    }

    public int countAllRecipesByAreaId(int areaId) {
        return recipeDao.countAllRecipesByAreaId(areaId);
    }

    private void validateRecipeRequest(RecipeRequest recipeRequest) {
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
        List<DetailedInstructions> updatedDetailedInstructions = detailedInstructionsDao.getDetailedInstructionsByRecipeId(recipe.getId())
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
                commentDao.getCommentsByRecipeId(recipe.getId()),
                nutritionDao.getNutritionByRecipeId(recipe.getId()),
                updatedDetailedInstructions
        );
    }
}