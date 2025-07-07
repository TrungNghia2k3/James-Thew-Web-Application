package com.ntn.culinary.dao;

import com.ntn.culinary.model.Comment;

import java.util.List;

public interface CommentDao {

    List<Comment> getCommentsByRecipeId (int id);
}
