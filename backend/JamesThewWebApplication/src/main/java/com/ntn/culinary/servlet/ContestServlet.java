package com.ntn.culinary.servlet;

import com.ntn.culinary.response.ContestResponse;
import com.ntn.culinary.service.ContestService;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.utils.ResponseUtils;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/api/contests")
public class ContestServlet extends HttpServlet {
    private final ContestService contestService = ContestService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");

        try {
            if (idParam != null) {
                handleGetById(idParam, resp);
            } else {
                handleGetAll(resp);
            }
        } catch (SQLException e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Database error: " + e.getMessage()));
        } catch (Exception e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    private void handleGetById(String idParam, HttpServletResponse resp) throws SQLException, IOException {
        try {
            int id = Integer.parseInt(idParam);
            ContestResponse contest = contestService.getContestById(id);

            if (contest != null) {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "Contest fetched successfully", contest));
            } else {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(404, "Contest with ID " + id + " does not exist"));
            }
        } catch (NumberFormatException e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(400, "Invalid ID format"));
        }
    }

    private void handleGetAll(HttpServletResponse resp) throws SQLException, IOException {
        // Giả định có phương thức getAllContests
        ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "All contests fetched", contestService.getAllContests()));
    }
}




