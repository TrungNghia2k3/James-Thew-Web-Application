package com.servlet;

import com.dao.UserDao;
import com.dto.request.UserRequest;
import com.dto.response.UserResponse;
import com.exception.UserNotFoundException;
import com.exception.ValidationException;
import com.google.gson.Gson;
import com.util.ResponseUtil;
import com.validator.UserValidator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();
    private final Gson gson = new Gson();
    private final ResponseUtil responseUtil = new ResponseUtil();
    private final UserValidator userValidator = new UserValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        // when id parameter is present, treat it as an 'getUserById' request
        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            try {
                UserResponse user = userDao.getUserById(id);
                responseUtil.sendSuccessResponse(resp, "Success", user);
            } catch (UserNotFoundException e) {
                responseUtil.sendNotFoundResponse(resp, "Error: " + e.getMessage());
            } catch (Exception e) {
                responseUtil.sendErrorResponse(resp, "Error: " + e.getMessage());
            }
        } else {
            // when id parameter is not present, treat it as an 'getAllUsers' request
            try {
                List<UserResponse> users = userDao.getAllUsers();
                responseUtil.sendSuccessResponse(resp, "Success", users);
            } catch (Exception e) {
                responseUtil.sendErrorResponse(resp, "Error: " + e.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            UserRequest user = gson.fromJson(req.getReader(), UserRequest.class);
            try {
                userValidator.validate(user);
                userDao.addUser(user);
                responseUtil.sendSuccessResponse(resp, "User added successfully", user);
            } catch (ValidationException e) {
                responseUtil.sendBadRequestResponse(resp, "Invalid user data: " + e.getMessage());
            }
        } catch (Exception e) {
            responseUtil.sendErrorResponse(resp, "Error in inserting user: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            UserRequest user = gson.fromJson(req.getReader(), UserRequest.class);
            try {
                userValidator.validate(user);
                userDao.editUser(user);
                responseUtil.sendSuccessResponse(resp, "User updated successfully", user);
            } catch (ValidationException e) {
                responseUtil.sendBadRequestResponse(resp, "Invalid user data: " + e.getMessage());
            } catch (UserNotFoundException e) {
                responseUtil.sendErrorResponse(resp, e.getMessage());
            }
        } catch (Exception e) {
            responseUtil.sendErrorResponse(resp, "Error in updating user: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            userDao.deleteUser(id);
            responseUtil.sendSuccessResponse(resp, "User deleted successfully", null);
        } catch (UserNotFoundException e) {
            responseUtil.sendErrorResponse(resp, e.getMessage());
        } catch (Exception e) {
            responseUtil.sendErrorResponse(resp, "Error in deleting user: " + e.getMessage());
        }
    }
}
