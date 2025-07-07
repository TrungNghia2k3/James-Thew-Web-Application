package com.ntn.culinary.servlet;

import com.ntn.culinary.dao.ContestDao;
import com.ntn.culinary.dao.impl.ContestDaoImpl;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.response.ContestResponse;
import com.ntn.culinary.service.ContestService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.ntn.culinary.utils.ResponseUtils.sendResponse;


@WebServlet("/api/contests")
public class ContestServlet extends HttpServlet {
    private final ContestService contestService;

    public ContestServlet() {
        ContestDao contestDao = new ContestDaoImpl();
        this.contestService = new ContestService(contestDao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String idParam = req.getParameter("id");

        try {
            if (idParam != null) {
                handleGetById(idParam, resp);
            } else {
                handleGetAll(resp);
            }
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    private void handleGetById(String idParam, HttpServletResponse resp) {
        try {
            int id = Integer.parseInt(idParam);
            ContestResponse contest = contestService.getContestById(id);

            if (contest != null) {
                sendResponse(resp, new ApiResponse<>(200, "Contest fetched successfully", contest));
            } else {
                sendResponse(resp, new ApiResponse<>(404, "Contest with ID " + id + " does not exist"));
            }
        } catch (NumberFormatException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid ID format"));
        }
    }

    private void handleGetAll(HttpServletResponse resp) {
        // Giả định có phương thức getAllContests
        sendResponse(resp, new ApiResponse<>(200, "All contests fetched", contestService.getAllContests()));
    }
}




