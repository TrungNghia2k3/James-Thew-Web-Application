package com.ntn.culinary.servlet;

import com.google.gson.Gson;
import com.ntn.culinary.util.ValidationUtil;
import com.ntn.culinary.request.LoginRequest;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.service.AuthService;
import com.ntn.culinary.util.ResponseUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    private static final Gson gson = new Gson();
    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        // Read JSON payload
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

        // Parse JSON
        LoginRequest loginRequest = gson.fromJson(sb.toString(), LoginRequest.class);

        // Validate input
        if (ValidationUtil.isNullOrEmpty(loginRequest.getUsername()) ||
                ValidationUtil.isNullOrEmpty(loginRequest.getPassword())) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(400, "Username and password are required"));
            return;
        }

        // Authenticate
        try {
            String jwt = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            if (jwt != null) {
                ResponseUtil.sendResponse(resp, new ApiResponse<>(200, "Login successful", jwt));
            } else {
                ResponseUtil.sendResponse(resp, new ApiResponse<>(401, "Invalid credentials"));
            }
        } catch (Exception e) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }
}