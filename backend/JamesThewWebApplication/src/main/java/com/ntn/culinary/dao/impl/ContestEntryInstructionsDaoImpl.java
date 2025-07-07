package com.ntn.culinary.dao.impl;

import com.ntn.culinary.dao.ContestEntryInstructionsDao;
import com.ntn.culinary.model.ContestEntryInstruction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.ntn.culinary.utils.DatabaseUtils.getConnection;

public class ContestEntryInstructionsDaoImpl implements ContestEntryInstructionsDao {

    @Override
    public void addContestEntryInstructions(ContestEntryInstruction contestEntryInstruction) {

        String INSERT_CONTEST_ENTRY_INSTRUCTIONS_QUERY = """
                INSERT INTO contest_entry_instructions (contest_entry_id, step_number, name, text, image)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CONTEST_ENTRY_INSTRUCTIONS_QUERY)) {

            stmt.setInt(1, contestEntryInstruction.getContestEntryId());
            stmt.setInt(2, contestEntryInstruction.getStepNumber());
            stmt.setString(3, contestEntryInstruction.getName());
            stmt.setString(4, contestEntryInstruction.getText());
            stmt.setString(5, contestEntryInstruction.getImage());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding contest entry instructions", e);
        }
    }
}
