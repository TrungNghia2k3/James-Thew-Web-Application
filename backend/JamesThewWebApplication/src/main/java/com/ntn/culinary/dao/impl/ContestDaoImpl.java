package com.ntn.culinary.dao.impl;

import com.ntn.culinary.dao.ContestDao;
import com.ntn.culinary.utils.DatabaseUtils;
import com.ntn.culinary.model.Contest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContestDaoImpl implements ContestDao {

    @Override
    public List<Contest> getAllContests() {

        String SELECT_ALL_CONTEST = "SELECT * FROM contests";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_CONTEST)) {

            List<Contest> contests = new ArrayList<>();

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Contest contest = new Contest();
                    contest.setId(rs.getInt("id"));
                    contest.setArticleBody(rs.getString("article_body"));
                    contest.setHeadline(rs.getString("headline"));
                    contest.setDescription(rs.getString("description"));
                    contest.setDatePublished(rs.getDate("date_published"));
                    contest.setDateModified(rs.getDate("date_modified"));
                    contest.setAccessRole(rs.getString("access_role"));
                    contests.add(contest);
                }
                return contests;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("SQLException: " + ex.getMessage());
        }
    }

    @Override
    public Contest getContestById(int id) {

        String SELECT_CONTEST_BY_ID = "SELECT * FROM contests WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_CONTEST_BY_ID)) {
            stmt.setInt(1, id);


            try (ResultSet rs = stmt.executeQuery();) {
                if (rs.next()) {
                    Contest contest = new Contest();
                    contest.setId(rs.getInt("id"));
                    contest.setArticleBody(rs.getString("article_body"));
                    contest.setHeadline(rs.getString("headline"));
                    contest.setDescription(rs.getString("description"));
                    contest.setDatePublished(rs.getDate("date_published"));
                    contest.setDateModified(rs.getDate("date_modified"));
                    contest.setAccessRole(rs.getString("access_role"));

                    return contest;
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("SQLException: " + ex.getMessage());
        }
    }

    @Override
    public boolean existsById(int id) {

        String EXIST_BY_ID_QUERY = "SELECT 1 FROM contests WHERE id = ? LIMIT 1";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXIST_BY_ID_QUERY)) {

            stmt.setInt(1, id);
            return stmt.executeQuery().next();
        } catch (SQLException ex) {
            throw new RuntimeException("SQLException: " + ex.getMessage());
        }
    }
}