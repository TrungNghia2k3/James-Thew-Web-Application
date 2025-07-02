package com.ntn.culinary.servlet;

import com.ntn.culinary.request.RegisterRequest;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.utils.GsonUtils;
import com.ntn.culinary.utils.ValidationUtils;
import com.ntn.culinary.service.UserService;
import com.ntn.culinary.utils.ResponseUtils;

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
    private final UserService userService = UserService.getInstance();

    // Check again

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Read the request body and build Json string
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(400, "Invalid request payload"));
            return;
        }

        // Parse JSON string to RegisterRequest object using Gson
        RegisterRequest userRequest = GsonUtils.fromJson(sb.toString(), RegisterRequest.class);

        Map<String, String> errors = new HashMap<>();

        if (ValidationUtils.isNullOrEmpty(userRequest.getUsername())) {
            errors.put("username", "Username is required");
        }

        if (ValidationUtils.isNullOrEmpty(userRequest.getPassword())) {
            errors.put("password", "Password is required");
        }

        if (!errors.isEmpty()) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(400, "Validation failed", errors));
            return;
        }

        try {
            userService.register(userRequest);
            ResponseUtils.sendResponse(resp, new ApiResponse<>(201, "User created successfully", null));
        } catch (Exception e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }
}
