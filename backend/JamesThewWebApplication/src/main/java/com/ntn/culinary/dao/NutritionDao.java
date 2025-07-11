package com.ntn.culinary.dao;

import com.ntn.culinary.model.Nutrition;

public interface NutritionDao {
    Nutrition getNutritionByRecipeId(int recipeId);

    void addNutrition(Nutrition nutrition);

    void updateNutrition(Nutrition nutrition);

    void deleteNutritionByNutritionIdRecipeId(int nutritionId, int recipeId);

    boolean existsByNutritionIdAndRecipeId(int nutritionId,int recipeId);

    Nutrition getNutritionByNutritionIdAndRecipeId(int nutritionId,int recipeId);
}
