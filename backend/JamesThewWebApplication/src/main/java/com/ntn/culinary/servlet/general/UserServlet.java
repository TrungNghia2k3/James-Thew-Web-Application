package com.ntn.culinary.servlet.general;

import com.ntn.culinary.dao.UserDao;
import com.ntn.culinary.dao.impl.UserDaoImpl;
import com.ntn.culinary.request.UserRequest;
import com.ntn.culinary.utils.ValidationUtils;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.service.UserService;
import com.ntn.culinary.utils.CastUtils;
import com.ntn.culinary.utils.ResponseUtils;

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
    private final UserService userService;

    public UserServlet() {
        UserDao userDao = new UserDaoImpl();
        this.userService = new UserService(userDao);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            req.setCharacterEncoding("UTF-8");

            // Lấy thông tin từ JwtFilter
            List<String> roles = CastUtils.toStringList(req.getAttribute("roles"));

            if (roles == null || !roles.contains("GENERAL")) {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(403, "Access denied: GENERAL role required"));
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

            if (ValidationUtils.isNotExistId(id)) {
                errors.put("id", "ID is required");
            }

            if (email != null && ValidationUtils.isNullOrEmpty(email)) {
                errors.put("email", "Email must not be empty");
            } else if (email != null && !ValidationUtils.isValidEmail(email)) {
                errors.put("email", "Invalid email format");
            }

            if (firstName != null && ValidationUtils.isNullOrEmpty(firstName)) {
                errors.put("firstName", "First name must not be empty");
            }

            if (lastName != null && ValidationUtils.isNullOrEmpty(lastName)) {
                errors.put("lastName", "Last name must not be empty");
            }

            if (phone != null && ValidationUtils.isNullOrEmpty(phone)) {
                errors.put("phone", "Phone must not be empty");
            } else if (phone != null && !ValidationUtils.isValidPhone(phone)) {
                errors.put("phone", "Invalid phone format");
            }

            if (!errors.isEmpty()) {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(400, "Validation failed", errors));
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
            ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "User updated successfully", null));

        } catch (Exception e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }
}
