package com.ntn.culinary.servlet;

import com.ntn.culinary.dao.UserDao;
import com.ntn.culinary.dao.impl.UserDaoImpl;
import com.ntn.culinary.request.RegisterRequest;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.service.UserService;
import com.ntn.culinary.utils.GsonUtils;
import com.ntn.culinary.validator.RegisterRequestValidator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import static com.ntn.culinary.utils.ResponseUtils.sendResponse;

@WebServlet("/api/register")
public class RegisterServlet extends HttpServlet {
    private final UserService userService;

    public RegisterServlet() {
        UserDao userDao = new UserDaoImpl();
        this.userService = new UserService(userDao);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        // Read the request body and build Json string
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid request payload"));
            return;
        }

        // Parse JSON string to RegisterRequest object using Gson
        RegisterRequest userRequest = GsonUtils.fromJson(sb.toString(), RegisterRequest.class);

        RegisterRequestValidator validator = new RegisterRequestValidator();
        Map<String, String> errors = validator.validate(userRequest);

        if (!errors.isEmpty()) {
            sendResponse(resp, new ApiResponse<>(400, "Validation failed", errors));
            return;
        }

        try {
            userService.register(userRequest);
            sendResponse(resp, new ApiResponse<>(201, "User created successfully", null));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }
}
