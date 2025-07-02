package com.ntn.culinary.dao;

import com.ntn.culinary.model.ContestEntry;
import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.SQLException;

public class ContestEntryDAO {
    private static final ContestEntryDAO contestEntryDAO = new ContestEntryDAO();

    private ContestEntryDAO() {
        // Private constructor to prevent instantiation
    }

    public static ContestEntryDAO getInstance() {
        return contestEntryDAO;
    }

    private static final String INSERT_CONTEST_ENTRY_QUERY = """
            INSERT INTO contest_entry (contest_id, user_id, name, ingredients, instructions, image, prepare_time, cooking_time, yield, category, area, short_description, date_created, date_modified, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String SELECT_CONTEST_ENTRY_ID_BY_USER_ID_AND_CONTEST_ID_QUERY = """
            SELECT id FROM contest_entry WHERE user_id = ? AND contest_id = ?
            """;

    private static final String EXIST_BY_USER_ID_AND_CONTEST_ID_AND_NAME = """
            SELECT 1 FROM contest_entry WHERE user_id = ? AND contest_id = ? AND name = ? LIMIT 1
            """;

    private static final String UPDATE_CONTEST_ENTRY_STATUS_QUERY = """
            UPDATE contest_entry SET status = ? WHERE id = ?
            """;

    private static final String EXIST_BY_ID_QUERY = """
            SELECT 1 FROM contest_entry WHERE id = ? LIMIT 1
            """;

    public void addContestEntry(ContestEntry contestEntry) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(INSERT_CONTEST_ENTRY_QUERY)) {

            stmt.setInt(1, contestEntry.getContestId());
            stmt.setInt(2, contestEntry.getUserId());
            stmt.setString(3, contestEntry.getName());
            stmt.setString(4, contestEntry.getIngredients());
            stmt.setString(5, contestEntry.getInstructions());
            stmt.setString(6, contestEntry.getImage());
            stmt.setString(7, contestEntry.getPrepareTime());
            stmt.setString(8, contestEntry.getCookingTime());
            stmt.setString(9, contestEntry.getYield());
            stmt.setString(10, contestEntry.getCategory());
            stmt.setString(11, contestEntry.getArea());
            stmt.setString(12, contestEntry.getShortDescription());
            stmt.setDate(13, contestEntry.getDateCreated());
            stmt.setDate(14, contestEntry.getDateModified());
            stmt.setString(15, contestEntry.getStatus());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error adding contest entry", e);
        }
    }

    public int getContestEntryIdByUserIdAndContestId(int userId, int contestId) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(SELECT_CONTEST_ENTRY_ID_BY_USER_ID_AND_CONTEST_ID_QUERY)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, contestId);

            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    return -1; // Not found
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving contest entry ID", e);
        }
    }

    public boolean existsByUserIdAndContestIdAndName(int userId, int contestId, String name) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(EXIST_BY_USER_ID_AND_CONTEST_ID_AND_NAME)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, contestId);
            stmt.setString(3, name);

            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Error checking contest entry existence", e);
        }
    }

    public void updateContestEntryStatus(int contestEntryId, String status) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(UPDATE_CONTEST_ENTRY_STATUS_QUERY)) {

            stmt.setString(1, status);
            stmt.setInt(2, contestEntryId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating contest entry status", e);
        }
    }

    public boolean existsById(int contestEntryId) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(EXIST_BY_ID_QUERY)) {

            stmt.setInt(1, contestEntryId);

            try (var rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new SQLException("Error checking contest entry existence by ID", e);
        }
    }
}
