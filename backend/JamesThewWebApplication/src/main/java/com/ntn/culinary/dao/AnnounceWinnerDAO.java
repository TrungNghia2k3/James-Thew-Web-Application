package com.ntn.culinary.dao;

import com.ntn.culinary.model.AnnounceWinner;
import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AnnounceWinnerDAO {
    private static final AnnounceWinnerDAO instance = new AnnounceWinnerDAO();

    private AnnounceWinnerDAO() {
        // Private constructor to prevent instantiation
    }

    public static AnnounceWinnerDAO getInstance() {
        return instance;
    }

    private static final String INSERT_WINNER_QUERY = "INSERT INTO announce_winners (announcement_id, contest_entry_id, ranking) VALUES (?, ?, ?)";

    public void insertWinner(AnnounceWinner announceWinner) throws SQLException {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_WINNER_QUERY)) {
            stmt.setInt(1, announceWinner.getAnnouncementId());
            stmt.setInt(2, announceWinner.getContestEntryId());
            stmt.setString(3, announceWinner.getRanking());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error inserting winner: " + e.getMessage(), e);
        }
    }
}
