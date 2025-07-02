package com.ntn.culinary.dao;

import com.ntn.culinary.model.ContestEntryInstruction;
import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.SQLException;

public class ContestEntryInstructionsDAO {
    private static final ContestEntryInstructionsDAO contestEntryInstructionsDAO = new ContestEntryInstructionsDAO();

    private ContestEntryInstructionsDAO() {
        // Private constructor to prevent instantiation
    }

    public static ContestEntryInstructionsDAO getInstance() {
        return contestEntryInstructionsDAO;
    }

    private static final String INSERT_CONTEST_ENTRY_INSTRUCTIONS_QUERY = """
            INSERT INTO contest_entry_instructions (contest_entry_id, step_number, name, text, image)
            VALUES (?, ?, ?, ?, ?)
            """;

    public void addContestEntryInstructions(ContestEntryInstruction contestEntryInstruction) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(INSERT_CONTEST_ENTRY_INSTRUCTIONS_QUERY)) {

            stmt.setInt(1, contestEntryInstruction.getContestEntryId());
            stmt.setInt(2, contestEntryInstruction.getStepNumber());
            stmt.setString(3, contestEntryInstruction.getName());
            stmt.setString(4, contestEntryInstruction.getText());
            stmt.setString(5, contestEntryInstruction.getImage());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error adding contest entry instructions", e);
        }
    }
}
