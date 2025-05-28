package com.servlet;

import com.google.gson.Gson;
import com.request.UserRequest;
import com.response.ApiResponse;
import com.service.UserService;
import com.util.ResponseUtil;
import com.util.ValidationUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/register")
public class RegisterServlet extends HttpServlet {
    private static final Gson gson = new Gson();
    private static final UserService userService = new UserService();

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

        Map<String, String> errors = new HashMap<>();

        if (ValidationUtil.isNullOrEmpty(userRequest.getUsername())) {
            errors.put("username", "Username is required");
        }
        if (ValidationUtil.isNullOrEmpty(userRequest.getPassword())) {
            errors.put("password", "Password is required");
        }
        if (ValidationUtil.isNullOrEmpty(userRequest.getEmail())) {
            errors.put("email", "Email is required");
        }
        if (ValidationUtil.isNullOrEmpty(userRequest.getFirstName())) {
            errors.put("firstName", "First name is required");
        }
        if (ValidationUtil.isNullOrEmpty(userRequest.getLastName())) {
            errors.put("lastName", "Last name is required");
        }

        if (!errors.isEmpty()) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(400, "Validation failed", errors));
            return;
        }

        try {
            userService.register(userRequest);
            ResponseUtil.sendResponse(resp, new ApiResponse<>(201, "User created successfully", null));
        } catch (Exception e) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }
}
