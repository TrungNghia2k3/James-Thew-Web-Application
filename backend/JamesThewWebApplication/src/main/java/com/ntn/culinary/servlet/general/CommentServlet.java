package com.ntn.culinary.servlet.general;

import com.google.gson.JsonSyntaxException;
import com.ntn.culinary.dao.CommentDao;
import com.ntn.culinary.dao.RecipeDao;
import com.ntn.culinary.dao.UserDao;
import com.ntn.culinary.dao.impl.CommentDaoImpl;
import com.ntn.culinary.dao.impl.RecipeDaoImpl;
import com.ntn.culinary.dao.impl.UserDaoImpl;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.exception.ValidationException;
import com.ntn.culinary.request.CommentRequest;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.response.CommentResponse;
import com.ntn.culinary.service.CommentService;
import com.ntn.culinary.validator.CommentRequestValidator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.ntn.culinary.utils.GsonUtils.fromJson;
import static com.ntn.culinary.utils.HttpRequestUtils.readRequestBody;
import static com.ntn.culinary.utils.ResponseUtils.sendResponse;

@WebServlet("/api/protected/general/comments")
public class CommentServlet extends HttpServlet {
    private final CommentService commentService;

    public CommentServlet() {
        UserDao userDao = new UserDaoImpl();
        RecipeDao recipeDao = new RecipeDaoImpl();
        CommentDao commentDao = new CommentDaoImpl();
        this.commentService = new CommentService(userDao, recipeDao, commentDao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int userId = Integer.parseInt(req.getParameter("userId"));
            List<CommentResponse> comments = commentService.getAllCommentsByUserId(userId);
            sendResponse(resp, new ApiResponse<>(200, "Comments retrieved successfully", comments));
        } catch (NumberFormatException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid user ID format"));
        } catch (NotFoundException e) {
            sendResponse(resp, new ApiResponse<>(404, e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // Read JSON request body
            String json = readRequestBody(req);

            // Parse JSON to CommentRequest object
            CommentRequest commentRequest = fromJson(json, CommentRequest.class);

            // Validate input
            CommentRequestValidator validator = new CommentRequestValidator();
            Map<String, String> errors = validator.validate(commentRequest, false);
            if (!errors.isEmpty()) {
                throw new ValidationException("Validation failed", errors);
            }

            // Add comment
            commentService.addComment(commentRequest);
            sendResponse(resp, new ApiResponse<>(201, "Comment added successfully"));
        } catch (JsonSyntaxException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid JSON data"));
        } catch (IOException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid request payload"));
        } catch (NotFoundException e) {
            sendResponse(resp, new ApiResponse<>(404, e.getMessage()));
        } catch (ValidationException e) {
            sendResponse(resp, new ApiResponse<>(422, e.getMessage(), e.getErrors()));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Internal server error" + e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // Read JSON request body
            String json = readRequestBody(req);

            // Parse JSON to CommentRequest object
            CommentRequest commentRequest = fromJson(json, CommentRequest.class);

            // Validate input
            CommentRequestValidator validator = new CommentRequestValidator();
            Map<String, String> errors = validator.validate(commentRequest, true);
            if (!errors.isEmpty()) {
                throw new ValidationException("Validation failed", errors);
            }

            // Update comment
            commentService.updateComment(commentRequest);
            sendResponse(resp, new ApiResponse<>(200, "Comment updated successfully"));
        } catch (JsonSyntaxException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid JSON data"));
        } catch (IOException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid request payload"));
        } catch (NotFoundException e) {
            sendResponse(resp, new ApiResponse<>(404, e.getMessage()));
        } catch (ValidationException e) {
            sendResponse(resp, new ApiResponse<>(422, e.getMessage(), e.getErrors()));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Internal server error: " + e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int commentId = Integer.parseInt(req.getParameter("id"));
            commentService.deleteComment(commentId);
            sendResponse(resp, new ApiResponse<>(200, "Comment deleted successfully"));
        } catch (NumberFormatException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid comment ID format"));
        } catch (NotFoundException e) {
            sendResponse(resp, new ApiResponse<>(404, e.getMessage()));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Internal server error: " + e.getMessage()));
        }
    }
}
