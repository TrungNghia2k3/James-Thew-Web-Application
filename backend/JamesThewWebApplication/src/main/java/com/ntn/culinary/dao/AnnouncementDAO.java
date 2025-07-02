package com.ntn.culinary.dao;

import com.ntn.culinary.model.Announcement;
import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AnnouncementDAO {
    private static final AnnouncementDAO announcementDAO = new AnnouncementDAO();

    private AnnouncementDAO() {
        // Private constructor to prevent instantiation
    }

    public static AnnouncementDAO getInstance() {
        return announcementDAO;
    }

    private static final String INSERT_ANNOUNCEMENT_QUERY = "INSERT INTO announcements (title, announcement_date, description, contest_id) VALUES (?, ?, ?, ?)";

    private static final String EXIST_ANNOUNCEMENT_WITH_CONTEST_QUERY = "SELECT 1 FROM announcements WHERE contest_id = ? LIMIT 1";

    private static final String SELECT_ANNOUNCEMENT_ID_BY_CONTEST_QUERY = "SELECT id FROM announcements WHERE contest_id = ?";

    public void insertAnnouncement(Announcement announcement) throws SQLException {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_ANNOUNCEMENT_QUERY)) {
            stmt.setString(1, announcement.getTitle());
            stmt.setString(2, announcement.getDescription());
            stmt.setDate(3, announcement.getAnnouncementDate());
            stmt.setInt(4, announcement.getContestId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error inserting announcement: " + e.getMessage(), e);
        }
    }

    public boolean existsAnnouncementWithContest(int contestId) throws SQLException {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXIST_ANNOUNCEMENT_WITH_CONTEST_QUERY)) {
            stmt.setInt(1, contestId);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new SQLException("Error checking announcement existence: " + e.getMessage(), e);
        }
    }

    public int getAnnouncementIdByContestId(int contestId) throws SQLException {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ANNOUNCEMENT_ID_BY_CONTEST_QUERY)) {
            stmt.setInt(1, contestId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException("Announcement not found for contest ID: " + contestId);
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving announcement ID: " + e.getMessage(), e);
        }
    }
}
