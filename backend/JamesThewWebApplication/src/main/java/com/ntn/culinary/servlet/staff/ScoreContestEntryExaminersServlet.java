package com.ntn.culinary.servlet.staff;

import com.google.gson.JsonSyntaxException;
import com.ntn.culinary.dao.ContestEntryDao;
import com.ntn.culinary.dao.ContestEntryExaminersDao;
import com.ntn.culinary.dao.UserDao;
import com.ntn.culinary.dao.impl.ContestEntryDaoImpl;
import com.ntn.culinary.dao.impl.ContestEntryExaminersDaoImpl;
import com.ntn.culinary.dao.impl.UserDaoImpl;
import com.ntn.culinary.exception.ConflictException;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.exception.ValidationException;
import com.ntn.culinary.model.ContestEntryExaminers;
import com.ntn.culinary.request.ContestEntryExaminersRequest;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.service.ContestEntryExaminersService;
import com.ntn.culinary.utils.GsonUtils;
import com.ntn.culinary.validator.ContestEntryExaminersRequestValidator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.ntn.culinary.utils.HttpRequestUtils.readRequestBody;
import static com.ntn.culinary.utils.ResponseUtils.sendResponse;

@WebServlet("/api/protected/staff/score-contest-entry-examiners")
public class ScoreContestEntryExaminersServlet extends HttpServlet {

    private final ContestEntryExaminersService contestEntryExaminersService;

    public ScoreContestEntryExaminersServlet() {
        ContestEntryDao contestEntryDao = new ContestEntryDaoImpl();
        ContestEntryExaminersDao contestEntryExaminersDao = new ContestEntryExaminersDaoImpl();
        UserDao userDao = new UserDaoImpl();
        this.contestEntryExaminersService = new ContestEntryExaminersService(contestEntryDao, contestEntryExaminersDao, userDao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String idParam = req.getParameter("id");
            String contestEntryIdParam = req.getParameter("contestEntryId");
            String examinerIdParam = req.getParameter("examinerId");

            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                ContestEntryExaminers examiner = contestEntryExaminersService.getContestEntryExaminerById(id);
                if (examiner == null) {
                    throw new NotFoundException("Contest entry examiner not found with id: " + id);
                }
                sendResponse(resp, new ApiResponse<>(200, "Contest entry examiner fetched successfully", examiner));

            } else if (contestEntryIdParam != null) {
                int contestEntryId = Integer.parseInt(contestEntryIdParam);
                List<ContestEntryExaminers> contestEntryExaminers = contestEntryExaminersService.getContestEntryExaminersByContestEntryId(contestEntryId);
                if (contestEntryExaminers == null || contestEntryExaminers.isEmpty()) {
                    throw new NotFoundException("No contest entry examiners found for contest entry");
                }
                sendResponse(resp, new ApiResponse<>(200, "Contest entry examiners fetched successfully", contestEntryExaminers));

            } else if (examinerIdParam != null) {
                int examinerId = Integer.parseInt(examinerIdParam);
                List<ContestEntryExaminers> contestEntryExaminers = contestEntryExaminersService.getContestEntryExaminersByExaminerId(examinerId);
                if (contestEntryExaminers == null || contestEntryExaminers.isEmpty()) {
                    throw new NotFoundException("No contest entry examiners found for examiner");
                }
                sendResponse(resp, new ApiResponse<>(200, "Contest entry examiners fetched successfully", contestEntryExaminers));

            } else {
                throw new NotFoundException("No valid parameters provided");
            }

        } catch (NumberFormatException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid ID format"));
        } catch (NotFoundException e) {
            sendResponse(resp, new ApiResponse<>(404, e.getMessage()));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // Read request body and build JSON string
            String json = readRequestBody(req);

            // Parse JSON sting to UserRolesRequest object by Gson
            ContestEntryExaminersRequest contestEntryExaminersRequest = GsonUtils.fromJson(json, ContestEntryExaminersRequest.class);

            // Validate request
            ContestEntryExaminersRequestValidator validator = new ContestEntryExaminersRequestValidator();
            Map<String, String> errors = validator.validate(contestEntryExaminersRequest);
            if (!errors.isEmpty()) {
                throw new ValidationException("Validation failed", errors);
            }

            contestEntryExaminersService.addExaminer(contestEntryExaminersRequest);
            sendResponse(resp, new ApiResponse<>(200, "Contest entry examiners added successfully"));
        } catch (JsonSyntaxException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid JSON data"));
        } catch (IOException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid request payload"));
        } catch (NotFoundException e) {
            sendResponse(resp, new ApiResponse<>(404, e.getMessage()));
        } catch (ConflictException e) {
            sendResponse(resp, new ApiResponse<>(409, e.getMessage()));
        } catch (ValidationException e) {
            sendResponse(resp, new ApiResponse<>(422, e.getMessage(), e.getErrors()));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // Read request body and build JSON string
            String json = readRequestBody(req);

            // Parse JSON string to ContestEntryExaminersRequest object by Gson
            ContestEntryExaminersRequest contestEntryExaminersRequest = GsonUtils.fromJson(json, ContestEntryExaminersRequest.class);

            // Validate request
            ContestEntryExaminersRequestValidator validator = new ContestEntryExaminersRequestValidator();
            Map<String, String> errors = validator.validate(contestEntryExaminersRequest);
            if (!errors.isEmpty()) {
                throw new ValidationException("Validation failed", errors);
            }

            contestEntryExaminersService.updateExaminer(contestEntryExaminersRequest);
            sendResponse(resp, new ApiResponse<>(200, "Contest entry examiner updated successfully"));
        } catch (JsonSyntaxException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid JSON data"));
        } catch (IOException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid request payload"));
        } catch (NotFoundException e) {
            sendResponse(resp, new ApiResponse<>(404, e.getMessage()));
        } catch (ConflictException e) {
            sendResponse(resp, new ApiResponse<>(409, e.getMessage()));
        } catch (ValidationException e) {
            sendResponse(resp, new ApiResponse<>(422, e.getMessage(), e.getErrors()));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String contestEntryIdParam = req.getParameter("contestEntryId");
            String examinerIdParam = req.getParameter("examinerId");

            if (contestEntryIdParam == null || examinerIdParam == null) {
                throw new NotFoundException("Contest entry ID and examiner ID are required");
            }

            int contestEntryId = Integer.parseInt(contestEntryIdParam);
            int examinerId = Integer.parseInt(examinerIdParam);

            contestEntryExaminersService.deleteExaminer(contestEntryId, examinerId);
            sendResponse(resp, new ApiResponse<>(200, "Contest entry examiner deleted successfully"));
        } catch (NumberFormatException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid ID format"));
        } catch (NotFoundException e) {
            sendResponse(resp, new ApiResponse<>(404, e.getMessage()));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }
}

