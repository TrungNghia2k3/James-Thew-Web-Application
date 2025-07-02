package com.ntn.culinary.dao;

import com.ntn.culinary.model.Comment;
import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.SQLException;
import java.util.List;

public class CommentDAO {
    private static final CommentDAO commentDAO = new CommentDAO();

    private CommentDAO() {
        // Private constructor to prevent instantiation
    }

    public static CommentDAO getInstance() {
        return commentDAO;
    }

    private static final String SELECT_COMMENTS_BY_RECIPE_ID_QUERY = """
            SELECT * FROM comments WHERE recipe_id = ?
            """;

    public List<Comment> getCommentsByRecipeId(int recipeId) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(SELECT_COMMENTS_BY_RECIPE_ID_QUERY)) {

            stmt.setInt(1, recipeId);
            try (var rs = stmt.executeQuery()) {
                List<Comment> comments = new java.util.ArrayList<>();
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
        }
    }

}
