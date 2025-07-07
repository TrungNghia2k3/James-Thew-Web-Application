package com.ntn.culinary.dao.impl;

import com.ntn.culinary.dao.ContestEntryExaminersDao;
import com.ntn.culinary.model.ContestEntryExaminers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.ntn.culinary.utils.DatabaseUtils.getConnection;

public class ContestEntryExaminersDaoImpl implements ContestEntryExaminersDao {

    @Override
    public void addContestEntryExaminer(ContestEntryExaminers contestEntryExaminers) {

        String INSERT_CONTEST_ENTRY_EXAMINER_QUERY = """
                INSERT INTO contest_entry_examiners (contest_entry_id, examiner_id, score, feedback, exam_date)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CONTEST_ENTRY_EXAMINER_QUERY)) {

            stmt.setInt(1, contestEntryExaminers.getContestEntryId());
            stmt.setInt(2, contestEntryExaminers.getExaminerId());
            stmt.setDouble(3, contestEntryExaminers.getScore());
            stmt.setString(4, contestEntryExaminers.getFeedback());
            stmt.setTimestamp(5, contestEntryExaminers.getExamDate());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding contest entry examiner", e);
        }
    }

    @Override
    public boolean existsByContestEntryIdAndExaminerId(int contestEntryId, int examinerId) {

        String EXIST_BY_CONTEST_ENTRY_ID_AND_EXAMINER_ID_QUERY = """
                SELECT 1 FROM contest_entry_examiners WHERE contest_entry_id = ? AND examiner_id = ? LIMIT 1
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXIST_BY_CONTEST_ENTRY_ID_AND_EXAMINER_ID_QUERY)) {

            stmt.setInt(1, contestEntryId);
            stmt.setInt(2, examinerId);

            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking existence of contest entry examiner", e);
        }
    }
}
