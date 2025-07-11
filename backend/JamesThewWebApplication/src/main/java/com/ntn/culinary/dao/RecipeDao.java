package com.ntn.culinary.dao;

import com.ntn.culinary.model.Recipe;

import java.util.List;

public interface RecipeDao {
    void addRecipe(Recipe recipe);

    void updateRecipe(Recipe recipe);

    void deleteRecipe(int id);

    boolean existsById(int id);

    Recipe getRecipeById(int id);

    List<Recipe> getAllRecipes(int page, int size);

    List<Recipe> searchAndFilterRecipes(String keyword, String category, String area, int recipedBy, int page, int size);

    int countSearchAndFilterRecipes(String keyword, String category, String area, int recipedBy);

    int countAllRecipes();

    int countAllRecipesByUserId(int userId);

    int countAllFreeRecipesByCategoryId(int categoryId);

    int countAllRecipesByCategoryId(int categoryId);

    int countAllFreeRecipesByAreaId(int areaId);

    int countAllRecipesByAreaId(int areaId);

    List<Recipe> getAllRecipesByUserId(int userId, int page, int size);

    Recipe getFreeRecipeById(int id);

    List<Recipe> getAllFreeRecipes(int page, int size);

    List<Recipe> searchAndFilterFreeRecipes(String keyword, String category, String area, int recipedBy, String accessType, int page, int size);

    int countSearchAndFilterFreeRecipes(String keyword, String category, String area, int recipedBy, String accessType);

    int countAllFreeRecipes();

    List<Recipe> getAllFreeRecipesByCategoryId(int categoryId, int page, int size);

    List<Recipe> getAllRecipesByCategoryId(int categoryId, int page, int size);

    List<Recipe> getAllFreeRecipesByAreaId(int areaId, int page, int size);

    List<Recipe> getAllRecipesByAreaId(int areaId, int page, int size);


}
