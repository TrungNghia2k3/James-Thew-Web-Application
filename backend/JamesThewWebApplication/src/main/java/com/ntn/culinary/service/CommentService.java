package com.ntn.culinary.service;

import com.ntn.culinary.dao.CommentDao;
import com.ntn.culinary.dao.RecipeDao;
import com.ntn.culinary.dao.UserDao;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.model.Comment;
import com.ntn.culinary.request.CommentRequest;
import com.ntn.culinary.response.CommentResponse;

import java.sql.Timestamp;
import java.util.List;

public class CommentService {
    private final UserDao userDao;
    private final RecipeDao recipeDao;
    private final CommentDao commentDao;

    public CommentService(UserDao userDao, RecipeDao recipeDao, CommentDao commentDao) {
        this.userDao = userDao;
        this.recipeDao = recipeDao;
        this.commentDao = commentDao;
    }

    public List<CommentResponse> getAllCommentsByUserId(int userId) {
        if (!userDao.existsById(userId)) {
            throw new NotFoundException("User does not exist");
        }
        return commentDao.getAllCommentsByUserId(userId).stream()
                .map(this::mapCommentToResponse)
                .toList();
    }

    public void addComment(CommentRequest commentRequest) {
        validateCommentRequest(commentRequest);
        commentDao.addComment(mapRequestToComment(commentRequest));
    }

    public void updateComment(CommentRequest commentRequest) {
        if (!commentDao.existsById(commentRequest.getId())) {
            throw new NotFoundException("Comment does not exist");
        }
        validateCommentRequest(commentRequest);
        commentDao.updateComment(mapRequestToComment(commentRequest));
    }

    public void banComment(int id) {
        if (!commentDao.existsById(id)) {
            throw new NotFoundException("Comment does not exist");
        }
        commentDao.banCommentById(id);
    }

    public void deleteComment(int id) {
        if (!commentDao.existsById(id)) {
            throw new NotFoundException("Comment does not exist");
        }
        commentDao.deleteCommentById(id);
    }

    private void validateCommentRequest(CommentRequest commentRequest) {
        if (!userDao.existsById(commentRequest.getId())) {
            throw new NotFoundException("User does not exist");
        }
        if (!recipeDao.existsById(commentRequest.getRecipeId())) {
            throw new NotFoundException("Recipe does not exist");
        }
    }

    private Comment mapRequestToComment(CommentRequest request) {
        return new Comment(
                request.getId(),
                request.getUserId(),
                request.getRecipeId(),
                request.getContent(),
                (Timestamp) request.getDate(),
                request.getRating(),
                request.isBanned()
        );
    }

    private CommentResponse mapCommentToResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getUserId(),
                comment.getRecipeId(),
                comment.getContent(),
                comment.getDate(),
                comment.getRating(),
                comment.isBanned()
        );
    }
}
