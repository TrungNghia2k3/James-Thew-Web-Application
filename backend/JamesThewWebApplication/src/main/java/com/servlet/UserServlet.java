package com.servlet;

import com.google.gson.Gson;
import com.request.UserRequest;
import com.response.ApiResponse;
import com.response.UserResponse;
import com.service.UserService;
import com.util.ResponseUtil;
import com.util.ValidationUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/users")
public class UserServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String idParam = req.getParameter("id");

        try {
            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                UserResponse user = userService.getUserById(id);
                ResponseUtil.sendResponse(resp, new ApiResponse<>(200, "User found", user));
            } else {
                List<UserResponse> users = userService.getAllUsers();
                ResponseUtil.sendResponse(resp, new ApiResponse<>(200, "User list fetched", users));
            }
        } catch (Exception e) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(400, "Invalid request payload"));
            return;
        }

        UserRequest userRequest = gson.fromJson(sb.toString(), UserRequest.class);

        if (ValidationUtil.isNullOrEmpty(userRequest.getUsername()) ||
                ValidationUtil.isNullOrEmpty(userRequest.getPassword()) ||
                ValidationUtil.isNullOrEmpty(userRequest.getEmail())) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(400, "All fields are required"));
            return;
        }

        try {
            userService.register(userRequest);
            ResponseUtil.sendResponse(resp, new ApiResponse<>(201, "User created successfully", null));
        } catch (Exception e) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(400, "Invalid request payload"));
            return;
        }

        UserRequest userRequest = gson.fromJson(sb.toString(), UserRequest.class);

        if (ValidationUtil.isNotExistId(userRequest.getId())) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(400, "User ID is required"));
            return;
        }

        if (ValidationUtil.isNullOrEmpty(userRequest.getUsername()) ||
                ValidationUtil.isNullOrEmpty(userRequest.getPassword()) ||
                ValidationUtil.isNullOrEmpty(userRequest.getEmail())) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(400, "All fields are required"));
            return;
        }

        try {
            userService.editUser(userRequest);
            ResponseUtil.sendResponse(resp, new ApiResponse<>(200, "User updated successfully", null));
        } catch (Exception e) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        try {

            int id = Integer.parseInt(req.getParameter("id"));

            if (ValidationUtil.isNotExistId(id)) {
                ResponseUtil.sendResponse(resp, new ApiResponse<>(400, "User ID is required"));
                return;
            }

            userService.deleteUser(id);
            ResponseUtil.sendResponse(resp, new ApiResponse<>(200, "User deleted successfully", null));
        } catch (Exception e) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }
}
