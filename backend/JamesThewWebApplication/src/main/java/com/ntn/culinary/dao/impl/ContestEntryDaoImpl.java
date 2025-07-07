package com.ntn.culinary.dao.impl;

import com.ntn.culinary.dao.ContestEntryDao;
import com.ntn.culinary.model.ContestEntry;
import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContestEntryDaoImpl implements ContestEntryDao {

    @Override
    public void addContestEntry(ContestEntry contestEntry) {

        String INSERT_CONTEST_ENTRY_QUERY = """
                INSERT INTO contest_entry (contest_id, user_id, name, ingredients, instructions, image, prepare_time, cooking_time, yield, category, area, short_description, date_created, date_modified, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CONTEST_ENTRY_QUERY)) {

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
            throw new RuntimeException("Error adding contest entry", e);
        }
    }

    @Override
    public int getContestEntryIdByUserIdAndContestId(int userId, int contestId) {

        String SELECT_CONTEST_ENTRY_ID_BY_USER_ID_AND_CONTEST_ID_QUERY = """
                SELECT id FROM contest_entry WHERE user_id = ? AND contest_id = ?
                """;

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_CONTEST_ENTRY_ID_BY_USER_ID_AND_CONTEST_ID_QUERY)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, contestId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    return -1; // Not found
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving contest entry ID", e);
        }
    }

    @Override
    public boolean existsByUserIdAndContestIdAndName(int userId, int contestId, String name) {

        String EXIST_BY_USER_ID_AND_CONTEST_ID_AND_NAME = """
                SELECT 1 FROM contest_entry WHERE user_id = ? AND contest_id = ? AND name = ? LIMIT 1
                """;

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXIST_BY_USER_ID_AND_CONTEST_ID_AND_NAME)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, contestId);
            stmt.setString(3, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking contest entry existence", e);
        }
    }

    @Override
    public void updateContestEntryStatus(int contestEntryId, String status) {

        String UPDATE_CONTEST_ENTRY_STATUS_QUERY = """
                UPDATE contest_entry SET status = ? WHERE id = ?
                """;

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_CONTEST_ENTRY_STATUS_QUERY)) {

            stmt.setString(1, status);
            stmt.setInt(2, contestEntryId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating contest entry status", e);
        }
    }

    @Override
    public boolean existsById(int contestEntryId) {

        String EXIST_BY_ID_QUERY = """
                SELECT 1 FROM contest_entry WHERE id = ? LIMIT 1
                """;

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXIST_BY_ID_QUERY)) {

            stmt.setInt(1, contestEntryId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking contest entry existence by ID", e);
        }
    }

    @Override
    public ContestEntry getContestEntryById(int contestEntryId) {

        String SELECT_CONTEST_ENTRY_BY_ID_QUERY = """
                SELECT * FROM contest_entry WHERE id = ?
                """;

        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(SELECT_CONTEST_ENTRY_BY_ID_QUERY)) {

            stmt.setInt(1, contestEntryId);

            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ContestEntry contestEntry = new ContestEntry();
                    contestEntry.setId(rs.getInt("id"));
                    contestEntry.setContestId(rs.getInt("contest_id"));
                    contestEntry.setUserId(rs.getInt("user_id"));
                    contestEntry.setName(rs.getString("name"));
                    contestEntry.setIngredients(rs.getString("ingredients"));
                    contestEntry.setInstructions(rs.getString("instructions"));
                    contestEntry.setImage(rs.getString("image"));
                    contestEntry.setPrepareTime(rs.getString("prepare_time"));
                    contestEntry.setCookingTime(rs.getString("cooking_time"));
                    contestEntry.setYield(rs.getString("yield"));
                    contestEntry.setCategory(rs.getString("category"));
                    contestEntry.setArea(rs.getString("area"));
                    contestEntry.setShortDescription(rs.getString("short_description"));
                    contestEntry.setDateCreated(rs.getDate("date_created"));
                    contestEntry.setDateModified(rs.getDate("date_modified"));
                    contestEntry.setStatus(rs.getString("status"));

                    return contestEntry;
                } else {
                    return null; // Not found
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving contest entry by ID", e);
        }
    }
}
