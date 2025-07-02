package com.ntn.culinary.dao;

import com.ntn.culinary.model.Nutrition;
import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.SQLException;

public class NutritionDAO {
    private static final NutritionDAO nutritionDAO = new NutritionDAO();

    private NutritionDAO() {
        // Private constructor to prevent instantiation
    }

    public static NutritionDAO getInstance() {
        return nutritionDAO;
    }

    private static final String SELECT_NUTRITION_BY_RECIPE_ID_QUERY = """
            SELECT * FROM nutritions WHERE recipe_id = ?
            """;

    public Nutrition getNutritionByRecipeId(int recipeId) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(SELECT_NUTRITION_BY_RECIPE_ID_QUERY)) {

            stmt.setInt(1, recipeId);
            try (var rs = stmt.executeQuery()) {
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
        }
    }
}
