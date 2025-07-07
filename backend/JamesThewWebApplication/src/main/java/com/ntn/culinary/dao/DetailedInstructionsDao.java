package com.ntn.culinary.dao;

import com.ntn.culinary.model.DetailedInstructions;

import java.util.List;

public interface DetailedInstructionsDao {
    List<DetailedInstructions> getDetailedInstructionsByRecipeId(int recipeId);
}
