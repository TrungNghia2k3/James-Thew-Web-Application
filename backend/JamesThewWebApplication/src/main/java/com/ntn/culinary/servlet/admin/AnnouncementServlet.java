package com.ntn.culinary.servlet.admin;

import com.ntn.culinary.model.AnnounceWinner;
import com.ntn.culinary.request.AnnouncementRequest;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.service.AnnouncementService;
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

@WebServlet("/api/protected/admin/announcements")
public class AnnouncementServlet extends HttpServlet {

    private final AnnouncementService announcementService = AnnouncementService.getInstance();

    // Xem thông tin thông báo, có thể lọc theo ID, có thể lấy tất cả thông báo, chỉnh sửa thông báo, xóa thông báo

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Lấy thông tin từ JwtFilter
        List<String> roles = CastUtils.toStringList(req.getAttribute("roles"));

        if (roles == null || !roles.contains("ADMIN")) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(403, "Access denied: ADMIN role required"));
            return;
        }

        // Read JSON payload
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

        // Parse JSON
        AnnouncementRequest announcementRequest = GsonUtils.fromJson(sb.toString(), AnnouncementRequest.class);

        // Validate input
        Map<String, String> errors = validateAnnouncementRequest(announcementRequest);

        if (!errors.isEmpty()) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(400, "Validation errors", errors));
            return;
        }

        try {
            announcementService.addAnnouncement(announcementRequest);
            ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "Announcement created successfully"));

        } catch (Exception e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    private Map<String, String> validateAnnouncementRequest(AnnouncementRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (ValidationUtils.isNullOrEmpty(request.getTitle())) {
            errors.put("title", "Title is required");
        }

        if (ValidationUtils.isNullOrEmpty(request.getDescription())) {
            errors.put("description", "Description is required");
        }

        if (ValidationUtils.isNotExistId(request.getContestId())) {
            errors.put("contestId", "Contest ID is required and must exist");
        }

        if (request.getWinners() == null || request.getWinners().isEmpty()) {
            errors.put("winners", "Winners are required");
        } else {
            for (int i = 0; i < request.getWinners().size(); i++) {
                AnnounceWinner announceWinner = request.getWinners().get(i);

                if (ValidationUtils.isNotExistId(announceWinner.getContestEntryId())) {
                    errors.put("winners[" + i + "].contestEntryId", "Contest Entry ID is required for winner " + (i + 1));
                }

                if (ValidationUtils.isNullOrEmpty(announceWinner.getRanking())) {
                    errors.put("winners[" + i + "].ranking", "Ranking is required for winner " + (i + 1));
                }
            }
        }

        return errors;
    }
}
