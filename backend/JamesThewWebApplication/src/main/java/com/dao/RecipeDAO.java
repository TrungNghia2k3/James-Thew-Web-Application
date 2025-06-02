package com.dao;

import com.model.Recipe;
import com.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {
    private static final String INSERT_RECIPE_QUERY = """
            INSERT INTO recipes (
                name, category, area, instructions, image, ingredients, 
                published_on, reciped_by, prepare_time, cooking_time, yield, 
                short_description, access_type
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String SELECT_ALL_RECIPES_WITH_LIMIT_AND_OFFSET_QUERY = """
            SELECT * FROM recipes WHERE access_type = 'FREE' LIMIT ? OFFSET ?
            """;
    private static final String COUNT_ALL_RECIPES_QUERY = """
            SELECT COUNT(*) FROM recipes WHERE access_type = 'FREE'
            """;

    private static final String SELECT_RECIPE_BY_ID_QUERY = "SELECT * FROM recipes WHERE id = ? AND access_type = 'FREE'";

    public void addRecipe(Recipe recipe) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_RECIPE_QUERY)) {

            stmt.setString(1, recipe.getName());
            stmt.setString(2, recipe.getCategory());
            stmt.setString(3, recipe.getArea());
            stmt.setString(4, recipe.getInstructions());
            stmt.setString(5, recipe.getImage());
            stmt.setString(6, recipe.getIngredients());
            stmt.setDate(7, new java.sql.Date(recipe.getPublishedOn().getTime()));
            stmt.setInt(8, recipe.getRecipedBy());
            stmt.setString(9, recipe.getPrepareTime());
            stmt.setString(10, recipe.getCookingTime());
            stmt.setString(11, recipe.getYield());
            stmt.setString(12, recipe.getShortDescription());
            stmt.setString(13, recipe.getAccessType());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error adding recipe: " + e.getMessage(), e);
        }
    }

    public Recipe getFreeRecipeById(int id) throws SQLException {

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_RECIPE_BY_ID_QUERY)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRecipe(rs);
                }
            }
        }

        return null; // Không tìm thấy
    }

    public List<Recipe> getAllFreeRecipes(int page, int size) throws SQLException {
        List<Recipe> recipes = new ArrayList<>();
        int offset = (page - 1) * size;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_RECIPES_WITH_LIMIT_AND_OFFSET_QUERY)) {

            stmt.setInt(1, size);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
            }
        }
        return recipes;
    }

    public List<Recipe> searchAndFilterFreeRecipes(String keyword, String category, int page, int size) throws SQLException {
        List<Recipe> recipes = new ArrayList<>();
        int offset = (page - 1) * size;

        StringBuilder sql = new StringBuilder("SELECT * FROM recipes WHERE access_type = 'FREE'");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + keyword + "%");
        }
        if (category != null && !category.isEmpty()) {
            sql.append(" AND category = ?");
            params.add(category);
        }
        sql.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(offset);

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
            }
        }
        return recipes;
    }

    public int countSearchAndFilterFreeRecipes(String keyword, String category) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM recipes WHERE access_type = 'FREE'");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + keyword + "%");
        }
        if (category != null && !category.isEmpty()) {
            sql.append(" AND category = ?");
            params.add(category);
        }

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int countAllFreeRecipes() throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_ALL_RECIPES_QUERY);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private Recipe mapResultSetToRecipe(ResultSet rs) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setId(rs.getInt("id"));
        recipe.setName(rs.getString("name"));
        recipe.setCategory(rs.getString("category"));
        recipe.setArea(rs.getString("area"));
        recipe.setInstructions(rs.getString("instructions"));
        recipe.setImage(rs.getString("image"));
        recipe.setIngredients(rs.getString("ingredients"));
        recipe.setPublishedOn(rs.getDate("published_on"));
        recipe.setRecipedBy(rs.getInt("reciped_by"));
        recipe.setPrepareTime(rs.getString("prepare_time"));
        recipe.setCookingTime(rs.getString("cooking_time"));
        recipe.setYield(rs.getString("yield"));
        recipe.setShortDescription(rs.getString("short_description"));
        recipe.setAccessType(rs.getString("access_type"));
        return recipe;
    }
}