package com.ntn.culinary.dao.impl;

import com.ntn.culinary.dao.CommentDao;
import com.ntn.culinary.model.Comment;
import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentDaoImpl implements CommentDao {

    @Override
    public List<Comment> getCommentsByRecipeId(int recipeId) {

        String SELECT_COMMENTS_BY_RECIPE_ID_QUERY = """
                SELECT * FROM comments WHERE recipe_id = ?
                """;

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_COMMENTS_BY_RECIPE_ID_QUERY)) {

            stmt.setInt(1, recipeId);

            List<Comment> comments = new ArrayList<>();

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment();
                    comment.setId(rs.getInt("id"));
                    comment.setUserId(rs.getInt("user_id"));
                    comment.setRecipeId(rs.getInt("recipe_id"));
                    comment.setContent(rs.getString("content"));
                    comment.setDate(rs.getTimestamp("date"));
                    comment.setRating(rs.getInt("rating"));
                    comments.add(comment);
                }
                return comments;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving comments: " + e.getMessage(), e);
        }

    }
}
