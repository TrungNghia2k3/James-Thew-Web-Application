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
}