package com.ntn.culinary.dao;

import com.ntn.culinary.utils.DatabaseUtils;
import com.ntn.culinary.model.Contest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContestDAO {

    private static final ContestDAO contestDAO = new ContestDAO();

    private ContestDAO() {
        // Private constructor to prevent instantiation
    }

    public static ContestDAO getInstance() {
        return contestDAO;
    }

    private final ContestImagesDAO contestImagesDAO = ContestImagesDAO.getInstance();

    private static final String SELECT_ALL_CONTEST = "SELECT * FROM contests";
    private static final String SELECT_CONTEST_BY_ID = "SELECT * FROM contests WHERE id = ?";
    private static final String EXIST_BY_ID_QUERY = "SELECT 1 FROM contests WHERE id = ? LIMIT 1";

    public List<Contest> getAllContests() throws SQLException {
        List<Contest> contests = new ArrayList<>();

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_CONTEST);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Contest contest = new Contest();
                contest.setId(rs.getInt("id"));
                contest.setArticleBody(rs.getString("article_body"));
                contest.setHeadline(rs.getString("headline"));
                contest.setDescription(rs.getString("description"));
                contest.setDatePublished(rs.getDate("date_published"));
                contest.setDateModified(rs.getDate("date_modified"));
                contest.setAccessRole(rs.getString("access_role"));
                contest.setContestImages(contestImagesDAO.getContestImagesByContestId(rs.getInt("id")));

                contests.add(contest);

            }
            return contests;
        }
    }

    public Contest getContestById(int id) throws SQLException {
        Contest contest = null;

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_CONTEST_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    contest = new Contest();
                    contest.setId(rs.getInt("id"));
                    contest.setArticleBody(rs.getString("article_body"));
                    contest.setHeadline(rs.getString("headline"));
                    contest.setDescription(rs.getString("description"));
                    contest.setDatePublished(rs.getDate("date_published"));
                    contest.setDateModified(rs.getDate("date_modified"));
                    contest.setAccessRole(rs.getString("access_role"));
                    contest.setContestImages(contestImagesDAO.getContestImagesByContestId(rs.getInt("id")));
                }
            }
        }
        return contest;
    }

    public boolean existsById(int id) throws SQLException {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXIST_BY_ID_QUERY)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}