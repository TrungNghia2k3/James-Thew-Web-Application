package com.ntn.culinary.dao;

import com.ntn.culinary.model.Nutrition;

public interface NutritionDao {
    Nutrition getNutritionByRecipeId(int recipeId);
}
