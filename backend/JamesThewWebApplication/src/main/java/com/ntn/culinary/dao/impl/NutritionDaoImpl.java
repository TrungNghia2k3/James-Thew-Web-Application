package com.ntn.culinary.dao.impl;

import com.ntn.culinary.dao.NutritionDao;
import com.ntn.culinary.model.Nutrition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.ntn.culinary.utils.DatabaseUtils.getConnection;

public class NutritionDaoImpl implements NutritionDao {


    public Nutrition getNutritionByRecipeId(int recipeId) {

        String SELECT_NUTRITION_BY_RECIPE_ID_QUERY = """
                SELECT * FROM nutritions WHERE recipe_id = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_NUTRITION_BY_RECIPE_ID_QUERY)) {

            stmt.setInt(1, recipeId);
            try (ResultSet rs = stmt.executeQuery();) {
                if (rs.next()) {
                    Nutrition nutrition = new Nutrition();
                    nutrition.setId(rs.getInt("id"));
                    nutrition.setCalories(rs.getString("calories"));
                    nutrition.setFat(rs.getString("fat"));
                    nutrition.setCholesterol(rs.getString("cholesterol"));
                    nutrition.setSodium(rs.getString("sodium"));
                    nutrition.setCarbohydrate(rs.getString("carbohydrate"));
                    nutrition.setFiber(rs.getString("fiber"));
                    nutrition.setProtein(rs.getString("protein"));
                    nutrition.setRecipeId(rs.getInt("recipe_id"));

                    return nutrition;
                } else {
                    return null; // No nutrition data found for the given recipe ID
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQLException: " + e.getMessage());
        }
    }
}
