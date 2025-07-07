package com.ntn.culinary.servlet;

import com.ntn.culinary.dao.UserDao;
import com.ntn.culinary.dao.impl.UserDaoImpl;
import com.ntn.culinary.service.JwtService;
import com.ntn.culinary.utils.GsonUtils;
import com.ntn.culinary.utils.ValidationUtils;
import com.ntn.culinary.request.LoginRequest;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.service.AuthService;
import com.ntn.culinary.utils.ResponseUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static com.ntn.culinary.utils.ValidationUtils.isNullOrEmpty;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    private final AuthService authService;

    public LoginServlet() {
        UserDao userDao = new UserDaoImpl();
        JwtService jwtService = new JwtService();
        this.authService = new AuthService(userDao, jwtService);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        // Read request body and build JSON string
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

        // Parse JSON sting to LoginRequest object by Gson
        LoginRequest loginRequest = GsonUtils.fromJson(sb.toString(), LoginRequest.class);

        // Validate input
        if (isNullOrEmpty(loginRequest.getUsername()) ||
                isNullOrEmpty(loginRequest.getPassword())) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(400, "Username and password are required"));
            return;
        }

        // Authenticate
        try {
            String jwt = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            if (jwt != null) {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "Login successful", jwt));
            }
        } catch (Exception e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }
}