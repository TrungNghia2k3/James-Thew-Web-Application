package com.ntn.culinary.servlet.staff;

import com.ntn.culinary.constant.PermissionType;
import com.ntn.culinary.dao.ContestEntryDao;
import com.ntn.culinary.dao.ContestEntryExaminersDao;
import com.ntn.culinary.dao.UserDao;
import com.ntn.culinary.dao.impl.ContestEntryDaoImpl;
import com.ntn.culinary.dao.impl.ContestEntryExaminersDaoImpl;
import com.ntn.culinary.dao.impl.UserDaoImpl;
import com.ntn.culinary.request.ContestEntryExaminersRequest;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.service.ContestEntryExaminersService;
import com.ntn.culinary.utils.CastUtils;
import com.ntn.culinary.utils.GsonUtils;
import com.ntn.culinary.utils.ResponseUtils;
import com.ntn.culinary.utils.ValidationUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ntn.culinary.utils.CastUtils.toStringList;
import static com.ntn.culinary.utils.ResponseUtils.sendResponse;

@WebServlet("/api/protected/staff/contest-entry-examiners")
public class ContestEntryExaminersServlet extends HttpServlet {

    private final ContestEntryExaminersService contestEntryExaminersService;

    public ContestEntryExaminersServlet() {
        ContestEntryDao contestEntryDao = new ContestEntryDaoImpl();
        ContestEntryExaminersDao contestEntryExaminersDao = new ContestEntryExaminersDaoImpl();
        UserDao userDao = new UserDaoImpl();
        this.contestEntryExaminersService = new ContestEntryExaminersService(contestEntryDao, contestEntryExaminersDao, userDao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        // Lấy thông tin từ JwtFilter
        List<String> roles = toStringList(req.getAttribute("roles"));

        if (roles == null || !roles.contains("STAFF")) {
            sendResponse(resp, new ApiResponse<>(403, "Access denied: STAFF role required"));
            return;
        }

        List<String> permissions = toStringList(req.getAttribute("permissions"));

        if (permissions == null || !permissions.contains(String.valueOf(PermissionType.MANAGE_CONTESTS))) {
            sendResponse(resp, new ApiResponse<>(403, "Access denied: ANSWER_QUESTIONS permission required"));
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Lấy thông tin từ JwtFilter
        List<String> roles = toStringList(req.getAttribute("roles"));

        if (roles == null || !roles.contains("STAFF")) {
            sendResponse(resp, new ApiResponse<>(403, "Access denied: STAFF role required"));
            return;
        }

        List<String> permissions = toStringList(req.getAttribute("permissions"));

        if (permissions == null || !permissions.contains(String.valueOf(PermissionType.MANAGE_CONTESTS))) {
            sendResponse(resp, new ApiResponse<>(403, "Access denied: MANAGE_CONTESTS permission required"));
            return;
        }

        // Read request body and build JSON string
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid request payload"));
        }


        // Parse JSON sting to UserRolesRequest object by Gson
        ContestEntryExaminersRequest contestEntryExaminersRequest = GsonUtils.fromJson(sb.toString(), ContestEntryExaminersRequest.class);

        // Validate request
        Map<String, String> errors = validateContestEntryExaminersRequest(contestEntryExaminersRequest);

        if (!errors.isEmpty()) {
            sendResponse(resp, new ApiResponse<>(400, "Validation errors", errors));
            return;
        }

        try {
            contestEntryExaminersService.addExaminer(contestEntryExaminersRequest);
            sendResponse(resp, new ApiResponse<>(200, "Contest entry examiners added successfully"));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    private Map<String, String> validateContestEntryExaminersRequest(ContestEntryExaminersRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (ValidationUtils.isNotExistId(request.getContestEntryId())) {
            errors.put("contestEntryId", "Contest Entry ID is required and must exist");
        }

        if (ValidationUtils.isNotExistId(request.getExaminerId())) {
            errors.put("examinerId", "Examiner ID is required and must exist");
        }

        if (request.getScore() < 0 || request.getScore() > 10) {
            errors.put("score", "Score must be between 0 and 10");
        }

        return errors;
    }
}

