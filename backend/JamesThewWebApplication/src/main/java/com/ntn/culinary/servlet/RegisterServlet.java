package com.ntn.culinary.servlet;

import com.google.gson.Gson;
import com.ntn.culinary.request.RegisterRequest;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.util.ValidationUtil;
import com.ntn.culinary.service.UserService;
import com.ntn.culinary.util.ResponseUtil;

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
    private final UserService userService = new UserService();

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

        RegisterRequest userRequest = gson.fromJson(sb.toString(), RegisterRequest.class);

        Map<String, String> errors = new HashMap<>();

        if (ValidationUtil.isNullOrEmpty(userRequest.getUsername())) {
            errors.put("username", "Username is required");
        }

        if (ValidationUtil.isNullOrEmpty(userRequest.getPassword())) {
            errors.put("password", "Password is required");
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
