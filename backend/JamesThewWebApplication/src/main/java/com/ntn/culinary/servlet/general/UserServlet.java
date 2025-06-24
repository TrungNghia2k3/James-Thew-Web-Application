package com.ntn.culinary.servlet.general;

import com.google.gson.Gson;
import com.ntn.culinary.request.UserRequest;
import com.ntn.culinary.util.ValidationUtil;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.service.UserService;
import com.ntn.culinary.util.CastUtil;
import com.ntn.culinary.util.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/protected/general/users")
@MultipartConfig
public class UserServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");

            // Lấy thông tin từ JwtFilter
            List<String> roles = CastUtil.toStringList(req.getAttribute("roles"));

            if (roles == null || !roles.contains("GENERAL")) {
                ResponseUtil.sendResponse(resp, new ApiResponse<>(403, "Access denied: GENERAL role required"));
                return;
            }

            // Lấy dữ liệu form

            int id = Integer.parseInt(req.getParameter("id"));
            String email = req.getParameter("email");
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String phone = req.getParameter("phone");
            Part avatar = req.getPart("avatar");

            // Validate input

            Map<String, String> errors = new HashMap<>();

            if (ValidationUtil.isNotExistId(id)) {
                errors.put("id", "ID is required");
            }

            if (email != null && ValidationUtil.isNullOrEmpty(email)) {
                errors.put("email", "Email must not be empty");
            } else if (email != null && !ValidationUtil.isValidEmail(email)) {
                errors.put("email", "Invalid email format");
            }

            if (firstName != null && ValidationUtil.isNullOrEmpty(firstName)) {
                errors.put("firstName", "First name must not be empty");
            }

            if (lastName != null && ValidationUtil.isNullOrEmpty(lastName)) {
                errors.put("lastName", "Last name must not be empty");
            }

            if (phone != null && ValidationUtil.isNullOrEmpty(phone)) {
                errors.put("phone", "Phone must not be empty");
            } else if (phone != null && !ValidationUtil.isValidPhone(phone)) {
                errors.put("phone", "Invalid phone format");
            }

            if (!errors.isEmpty()) {
                ResponseUtil.sendResponse(resp, new ApiResponse<>(400, "Validation failed", errors));
                return;
            }

            // Create UserRequest object
            UserRequest userRequest = new UserRequest();
            userRequest.setId(id);
            userRequest.setEmail(email);
            userRequest.setFirstName(firstName);
            userRequest.setLastName(lastName);
            userRequest.setPhone(phone);

            userService.editGeneralUser(userRequest, avatar);
            ResponseUtil.sendResponse(resp, new ApiResponse<>(200, "User updated successfully", null));

        } catch (Exception e) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    }
}
