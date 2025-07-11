package com.ntn.culinary.dao.impl;

import com.ntn.culinary.dao.DetailedInstructionsDao;
import com.ntn.culinary.model.DetailedInstructions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.ntn.culinary.utils.DatabaseUtils.getConnection;

public class DetailedInstructionsDaoImpl implements DetailedInstructionsDao {

    @Override
    public List<DetailedInstructions> getDetailedInstructionsByRecipeId(int recipeId) {
        String SELECT_DETAILED_INSTRUCTIONS_BY_RECIPE_ID_QUERY = """
                SELECT * FROM detailed_instructions WHERE recipe_id = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_DETAILED_INSTRUCTIONS_BY_RECIPE_ID_QUERY)) {
            stmt.setInt(1, recipeId);

            List<DetailedInstructions> detailedInstructionsList = new ArrayList<>();

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DetailedInstructions detailedInstructions = new DetailedInstructions();
                    detailedInstructions.setId(rs.getInt("id"));
                    detailedInstructions.setName(rs.getString("name"));
                    detailedInstructions.setText(rs.getString("text"));
                    detailedInstructions.setImage(rs.getString("image"));
                    detailedInstructions.setRecipeId(rs.getInt("recipe_id"));

                    detailedInstructionsList.add(detailedInstructions);
                }
                return detailedInstructionsList;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error when trying to get detailed instructions list", e);
        }
    }

    @Override
    public void addDetailedInstructions(DetailedInstructions detailedInstructions) {
        String INSERT_DETAILED_INSTRUCTIONS_QUERY = """
                INSERT INTO detailed_instructions (name, text, image, recipe_id)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_DETAILED_INSTRUCTIONS_QUERY)) {

            stmt.setString(1, detailedInstructions.getName());
            stmt.setString(2, detailedInstructions.getText());
            stmt.setString(3, detailedInstructions.getImage());
            stmt.setInt(4, detailedInstructions.getRecipeId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding detailed instructions", e);
        }
    }

    @Override
    public void deleteDetailedInstructionsById(int id) {
        String DELETE_DETAILED_INSTRUCTIONS_QUERY = """
                DELETE FROM detailed_instructions WHERE id = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_DETAILED_INSTRUCTIONS_QUERY)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting detailed instructions", e);
        }
    }

    @Override
    public void updateDetailedInstructions(DetailedInstructions detailedInstructions) {
        String UPDATE_DETAILED_INSTRUCTIONS_QUERY = """
                UPDATE detailed_instructions
                SET name = ?, text = ?, image = ?, recipe_id = ?
                WHERE id = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_DETAILED_INSTRUCTIONS_QUERY)) {

            stmt.setString(1, detailedInstructions.getName());
            stmt.setString(2, detailedInstructions.getText());
            stmt.setString(3, detailedInstructions.getImage());
            stmt.setInt(4, detailedInstructions.getRecipeId());
            stmt.setInt(5, detailedInstructions.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating detailed instructions", e);
        }
    }

    @Override
    public boolean existsById(int id) {
        String EXIST_BY_ID_QUERY = "SELECT 1 FROM detailed_instructions WHERE id = ? LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXIST_BY_ID_QUERY)) {

            stmt.setInt(1, id);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking existence of detailed instructions by ID", e);
        }
    }
}