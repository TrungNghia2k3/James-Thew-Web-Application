package com.ntn.culinary.dao;

import com.ntn.culinary.model.ContestEntryExaminers;
import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.SQLException;

public class ContestEntryExaminersDAO {
    private static final ContestEntryExaminersDAO contestEntryExaminersDAO = new ContestEntryExaminersDAO();

    private ContestEntryExaminersDAO() {
        // Private constructor to prevent instantiation
    }

    public static ContestEntryExaminersDAO getInstance() {
        return contestEntryExaminersDAO;
    }

    private static final String INSERT_CONTEST_ENTRY_EXAMINER_QUERY = """
            INSERT INTO contest_entry_examiners (contest_entry_id, examiner_id, score, feedback, exam_date)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String EXIST_BY_CONTEST_ENTRY_ID_AND_EXAMINER_ID_QUERY = """
            SELECT 1 FROM contest_entry_examiners WHERE contest_entry_id = ? AND examiner_id = ? LIMIT 1
            """;

    public void addContestEntryExaminer(ContestEntryExaminers contestEntryExaminers) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(INSERT_CONTEST_ENTRY_EXAMINER_QUERY)) {

            stmt.setInt(1, contestEntryExaminers.getContestEntryId());
            stmt.setInt(2, contestEntryExaminers.getExaminerId());
            stmt.setDouble(3, contestEntryExaminers.getScore());
            stmt.setString(4, contestEntryExaminers.getFeedback());
            stmt.setTimestamp(5, contestEntryExaminers.getExamDate());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error adding contest entry examiner", e);
        }
    }

    public boolean existsByContestEntryIdAndExaminerId(int contestEntryId, int examinerId) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(EXIST_BY_CONTEST_ENTRY_ID_AND_EXAMINER_ID_QUERY)) {

            stmt.setInt(1, contestEntryId);
            stmt.setInt(2, examinerId);

            try (var rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new SQLException("Error checking existence of contest entry examiner", e);
        }
    }
}
