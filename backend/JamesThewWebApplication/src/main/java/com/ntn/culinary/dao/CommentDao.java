package com.ntn.culinary.dao;

import com.ntn.culinary.model.Comment;

import java.util.List;

public interface CommentDao {

    List<Comment> getCommentsByRecipeId (int id);

    void addComment(Comment comment);

    void deleteCommentById(int id);

    void updateComment(Comment comment);

    boolean existsById(int id);

    void banCommentById(int id);

    List<Comment> getAllCommentsByUserId(int userId);
}
