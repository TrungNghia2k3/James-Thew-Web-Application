package com.ntn.culinary.servlet.staff;

import com.ntn.culinary.dao.*;
import com.ntn.culinary.dao.impl.*;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.response.ContestEntryResponse;
import com.ntn.culinary.service.ContestEntryService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.ntn.culinary.utils.ResponseUtils.sendResponse;

@WebServlet("/api/protected/staff/contest-entries")
public class ContestEntryServlet extends HttpServlet {
    private final ContestEntryService contestEntryService;

    public ContestEntryServlet() {
        ContestEntryDao contestEntryDao = new ContestEntryDaoImpl();
        ContestEntryInstructionsDao contestEntryInstructionsDao = new ContestEntryInstructionsDaoImpl();
        UserDao userDao = new UserDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        AreaDao areaDao = new AreaDaoImpl();
        ContestDao contestDao = new ContestDaoImpl();
        this.contestEntryService = new ContestEntryService(contestEntryDao, contestEntryInstructionsDao, userDao, categoryDao, areaDao, contestDao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String contestIdParam = req.getParameter("contestId");
            if (contestIdParam != null) {
                int contestId = Integer.parseInt(contestIdParam);
                List<ContestEntryResponse> contestEntries = contestEntryService.getContestEntriesByContestId(contestId);
                sendResponse(resp, new ApiResponse<>(200, "Contest entries retrieved successfully", contestEntries));
            } else {
                throw new NotFoundException("Contest ID is required to retrieve contest entries");
            }
        } catch (NumberFormatException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid announcement ID format"));
        } catch (NotFoundException e) {
            sendResponse(resp, new ApiResponse<>(404, e.getMessage()));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }
}
