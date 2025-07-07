package com.ntn.culinary.dao;

import com.ntn.culinary.model.Recipe;

import java.util.List;

public interface RecipeDao {
    void addRecipe(Recipe recipe);

    Recipe getFreeRecipeById(int id);

    List<Recipe> getAllFreeRecipes(int page, int size);

    List<Recipe> searchAndFilterFreeRecipes(String keyword, String category, String area, int recipedBy, String accessType, int page, int size);

    int countSearchAndFilterFreeRecipes(String keyword, String category, String area, int recipedBy, String accessType);

    int countAllFreeRecipes();

}
