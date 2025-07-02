package com.ntn.culinary.dao;

import com.ntn.culinary.model.DetailedInstructions;
import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.SQLException;
import java.util.List;

public class DetailedInstructionsDAO {
    private static final DetailedInstructionsDAO detailedInstructionsDAO = new DetailedInstructionsDAO();

    private DetailedInstructionsDAO() {
        // Private constructor to prevent instantiation
    }

    public static DetailedInstructionsDAO getInstance() {
        return detailedInstructionsDAO;
    }

    private static final String SELECT_DETAILED_INSTRUCTIONS_BY_RECIPE_ID_QUERY = """
            SELECT * FROM detailed_instructions WHERE recipe_id = ?
            """;

    public List<DetailedInstructions> getDetailedInstructionsByRecipeId(int recipeId) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(SELECT_DETAILED_INSTRUCTIONS_BY_RECIPE_ID_QUERY)) {

            stmt.setInt(1, recipeId);
            try (var rs = stmt.executeQuery()) {
                List<DetailedInstructions> detailedInstructionsList = new java.util.ArrayList<>();
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
        }

    }
}
