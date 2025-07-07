package com.ntn.culinary.dao.impl;

import com.ntn.culinary.dao.RecipeDao;
import com.ntn.culinary.model.Recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.ntn.culinary.utils.DatabaseUtils.getConnection;

public class RecipeDaoImpl implements RecipeDao {
    public void addRecipe(Recipe recipe) {

        String INSERT_RECIPE_QUERY = """
                INSERT INTO recipes (
                    name, category, area, instructions, image, ingredients, 
                    published_on, reciped_by, prepare_time, cooking_time, yield, 
                    short_description, access_type
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = getConnection();
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
            throw new RuntimeException("Error adding recipe: " + e.getMessage(), e);
        }
    }

    @Override
    public Recipe getFreeRecipeById(int id) {

        String SELECT_RECIPE_BY_ID_QUERY = "SELECT * FROM recipes WHERE id = ? AND access_type = 'FREE'";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_RECIPE_BY_ID_QUERY)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRecipe(rs);
                } else {
                    return null;
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("SQL Exception: " + ex.getMessage(), ex);
        }

    }

    @Override
    public List<Recipe> getAllFreeRecipes(int page, int size) {

        String SELECT_ALL_RECIPES_WITH_LIMIT_AND_OFFSET_QUERY = """
                SELECT * FROM recipes WHERE access_type = 'FREE' LIMIT ? OFFSET ?
                """;

        List<Recipe> recipes = new ArrayList<>();
        int offset = (page - 1) * size;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_RECIPES_WITH_LIMIT_AND_OFFSET_QUERY)) {

            stmt.setInt(1, size);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
                return recipes;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("SQL Exception: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Recipe> searchAndFilterFreeRecipes(String keyword, String category,
                                                   String area, int recipedBy,
                                                   String accessType, int page, int size) {
        List<Recipe> recipes = new ArrayList<>();
        int offset = (page - 1) * size;

        StringBuilder sql = new StringBuilder("SELECT * FROM recipes");
        List<Object> params = new ArrayList<>();

        boolean hasCondition = false;

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(hasCondition ? " AND" : " WHERE");
            sql.append(" name LIKE ?");
            params.add("%" + keyword + "%");
            hasCondition = true;
        }

        if (category != null && !category.isEmpty()) {
            sql.append(hasCondition ? " AND" : " WHERE");
            sql.append(" category = ?");
            params.add(category);
            hasCondition = true;
        }

        if (area != null && !area.isEmpty()) {
            sql.append(hasCondition ? " AND" : " WHERE");
            sql.append(" area = ?");
            params.add(area);
            hasCondition = true;
        }

        if (recipedBy > 0) {
            sql.append(hasCondition ? " AND" : " WHERE");
            sql.append(" reciped_by = ?");
            params.add(recipedBy);
            hasCondition = true;
        }

        if (accessType != null && !accessType.isEmpty()) {
            sql.append(hasCondition ? " AND" : " WHERE");
            sql.append(" access_type = ?");
            params.add(accessType);
            hasCondition = true;
        }

        sql.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(offset);

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
                return recipes;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("SQL Exception: " + ex.getMessage(), ex);
        }
    }

    @Override
    public int countSearchAndFilterFreeRecipes(String keyword, String category,
                                               String area, int recipedBy,
                                               String accessType) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM recipes");
        List<Object> params = new ArrayList<>();

        boolean hasCondition = false;

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(hasCondition ? " AND" : " WHERE");
            sql.append(" name LIKE ?");
            params.add("%" + keyword + "%");
            hasCondition = true;
        }

        if (category != null && !category.isEmpty()) {
            sql.append(hasCondition ? " AND" : " WHERE");
            sql.append(" category = ?");
            params.add(category);
            hasCondition = true;
        }

        if (area != null && !area.isEmpty()) {
            sql.append(hasCondition ? " AND" : " WHERE");
            sql.append(" area = ?");
            params.add(area);
            hasCondition = true;
        }

        if (recipedBy > 0) {
            sql.append(hasCondition ? " AND" : " WHERE");
            sql.append(" reciped_by = ?");
            params.add(recipedBy);
            hasCondition = true;
        }

        if (accessType != null && !accessType.isEmpty()) {
            sql.append(hasCondition ? " AND" : " WHERE");
            sql.append(" access_type = ?");
            params.add(accessType);
            hasCondition = true;
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("SQL Exception: " + ex.getMessage(), ex);
        }
    }

    @Override
    public int countAllFreeRecipes() {

        String COUNT_ALL_RECIPES_QUERY = """
                SELECT COUNT(*) FROM recipes WHERE access_type = 'FREE'
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_ALL_RECIPES_QUERY);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException ex) {
            throw new RuntimeException("SQL Exception: " + ex.getMessage(), ex);
        }

    }

    private Recipe mapResultSetToRecipe(ResultSet rs) {
        try {
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
        } catch (SQLException ex) {
            throw new RuntimeException("SQL Exception: " + ex.getMessage(), ex);
        }
    }
}
