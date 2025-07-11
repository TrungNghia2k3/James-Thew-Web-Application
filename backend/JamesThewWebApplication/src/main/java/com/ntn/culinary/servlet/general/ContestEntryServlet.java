package com.ntn.culinary.servlet.general;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ntn.culinary.dao.*;
import com.ntn.culinary.dao.impl.*;
import com.ntn.culinary.exception.ConflictException;
import com.ntn.culinary.exception.ForbiddenException;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.exception.ValidationException;
import com.ntn.culinary.model.ContestEntryInstruction;
import com.ntn.culinary.request.ContestEntryRequest;
import com.ntn.culinary.request.DeleteContestEntryRequest;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.response.ContestEntryResponse;
import com.ntn.culinary.service.ContestEntryService;
import com.ntn.culinary.utils.CastUtils;
import com.ntn.culinary.utils.GsonUtils;
import com.ntn.culinary.utils.ResponseUtils;
import com.ntn.culinary.utils.ValidationUtils;
import com.ntn.culinary.validator.ContestEntryRequestValidator;
import com.ntn.culinary.validator.DeleteContestEntryRequestValidator;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ntn.culinary.utils.CastUtils.toStringList;
import static com.ntn.culinary.utils.HttpRequestUtils.readRequestBody;
import static com.ntn.culinary.utils.ResponseUtils.sendResponse;

@WebServlet("/api/protected/general/contest-entry")
@MultipartConfig
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
            String idParam = req.getParameter("id");
            String contestIdParam = req.getParameter("contestId");
            String userIdParam = req.getParameter("userId");

            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                ContestEntryResponse contestEntry = contestEntryService.getContestEntryById(id);
                sendResponse(resp, new ApiResponse<>(200, "Contest entry fetched successfully", contestEntry));
            } else if (contestIdParam != null && userIdParam != null) {
                int contestId = Integer.parseInt(contestIdParam);
                int userId = Integer.parseInt(userIdParam);
                ContestEntryResponse contestEntry = contestEntryService.getContestEntryByUserIdAndContestId(userId, contestId);
                sendResponse(resp, new ApiResponse<>(200, "Contest entry fetched successfully", contestEntry));
            } else if (userIdParam != null) {
                int userId = Integer.parseInt(userIdParam);
                List<ContestEntryResponse> userContestEntries = contestEntryService.getContestEntriesByUserId(userId);
                sendResponse(resp, new ApiResponse<>(200, "User's contest entries fetched successfully", userContestEntries));
            }
        } catch (NumberFormatException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid ID format"));
        } catch (NotFoundException e) {
            sendResponse(resp, new ApiResponse<>(404, e.getMessage()));
        } catch (ValidationException e) {
            sendResponse(resp, new ApiResponse<>(422, e.getMessage(), e.getErrors()));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");

            // Lấy dữ liệu từ form
            int contestId = Integer.parseInt(req.getParameter("contestId"));
            int userId = Integer.parseInt(req.getParameter("userId"));
            String name = req.getParameter("name");
            String ingredients = req.getParameter("ingredients");
            Part imagePart = req.getPart("image");
            String prepareTime = req.getParameter("prepareTime");
            String cookingTime = req.getParameter("cookingTime");
            String yield = req.getParameter("yield");
            String category = req.getParameter("category");
            String area = req.getParameter("area");
            String shortDescription = req.getParameter("shortDescription");
            String json = req.getParameter("contestEntryInstructions");

            // Type : Interface biểu diễn kiểu dữ liệu.
            // TypeToken<List<Instruction>> : Khai báo 1 type token đại diện cho List<Instruction>.
            // new TypeToken<...>() {} : Tạo 1 anonymous class để Gson giữ thông tin generic.
            // .getType() : Lấy Type object từ TypeToken.

            Type listType = new TypeToken<List<ContestEntryInstruction>>() {
            }.getType();
            List<ContestEntryInstruction> instructions = GsonUtils.getGson().fromJson(json, listType);

            // Set values to ContestEntryRequest
            ContestEntryRequest contestEntryRequest = new ContestEntryRequest();
            contestEntryRequest.setContestId(contestId);
            contestEntryRequest.setUserId(userId);
            contestEntryRequest.setName(name);
            contestEntryRequest.setIngredients(ingredients);
            contestEntryRequest.setPrepareTime(prepareTime);
            contestEntryRequest.setCookingTime(cookingTime);
            contestEntryRequest.setYield(yield);
            contestEntryRequest.setCategory(category);
            contestEntryRequest.setArea(area);
            contestEntryRequest.setShortDescription(shortDescription);
            contestEntryRequest.setContestEntryInstructions(instructions);

            // Validate input
            ContestEntryRequestValidator validator = new ContestEntryRequestValidator();
            Map<String, String> errors = validator.validateContestEntryRequest(contestEntryRequest, imagePart, false);
            if (!errors.isEmpty()) {
                throw new ValidationException("Validation failed", errors);
            }

            contestEntryService.addContestEntry(contestEntryRequest, imagePart);
            sendResponse(resp, new ApiResponse<>(200, "Contest entry created successfully", null));

        } catch (JsonSyntaxException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid JSON data"));
        } catch (IOException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid request payload"));
        } catch (NumberFormatException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid number format: " + e.getMessage()));
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
            req.setCharacterEncoding("UTF-8");

            // Lấy dữ liệu từ form
            int id = Integer.parseInt(req.getParameter("id"));
            int contestId = Integer.parseInt(req.getParameter("contestId"));
            int userId = Integer.parseInt(req.getParameter("userId"));
            String name = req.getParameter("name");
            String ingredients = req.getParameter("ingredients");
            Part imagePart = req.getPart("image");
            String prepareTime = req.getParameter("prepareTime");
            String cookingTime = req.getParameter("cookingTime");
            String yield = req.getParameter("yield");
            String category = req.getParameter("category");
            String area = req.getParameter("area");
            String shortDescription = req.getParameter("shortDescription");
            String json = req.getParameter("contestEntryInstructions");

            Type listType = new TypeToken<List<ContestEntryInstruction>>() {
            }.getType();
            List<ContestEntryInstruction> instructions = GsonUtils.getGson().fromJson(json, listType);

            // Set values to ContestEntryRequest
            ContestEntryRequest contestEntryRequest = new ContestEntryRequest();
            contestEntryRequest.setId(id);
            contestEntryRequest.setContestId(contestId);
            contestEntryRequest.setUserId(userId);
            contestEntryRequest.setName(name);
            contestEntryRequest.setIngredients(ingredients);
            contestEntryRequest.setPrepareTime(prepareTime);
            contestEntryRequest.setCookingTime(cookingTime);
            contestEntryRequest.setYield(yield);
            contestEntryRequest.setCategory(category);
            contestEntryRequest.setArea(area);
            contestEntryRequest.setShortDescription(shortDescription);
            contestEntryRequest.setContestEntryInstructions(instructions);

            // Validate input
            ContestEntryRequestValidator validator = new ContestEntryRequestValidator();
            Map<String, String> errors = validator.validateContestEntryRequest(contestEntryRequest, imagePart, true);
            if (!errors.isEmpty()) {
                throw new ValidationException("Validation failed", errors);
            }

            contestEntryService.updateContestEntry(contestEntryRequest, imagePart);
            sendResponse(resp, new ApiResponse<>(200, "Contest entry updated successfully", null));

        } catch (JsonSyntaxException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid JSON data"));
        } catch (IOException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid request payload"));
        } catch (NumberFormatException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid number format: " + e.getMessage()));
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
            // Read JSON payload
            String json = readRequestBody(req);

            // Parse JSON
            DeleteContestEntryRequest deleteContestEntryRequest = GsonUtils.fromJson(json, DeleteContestEntryRequest.class);

            // Validate input
            DeleteContestEntryRequestValidator validator = new DeleteContestEntryRequestValidator();
            Map<String, String> errors = validator.validate(deleteContestEntryRequest);
            if (!errors.isEmpty()) {
                throw new ValidationException("Validation failed", errors);
            }

            contestEntryService.deleteContestEntry(deleteContestEntryRequest);
            sendResponse(resp, new ApiResponse<>(200, "Contest entry deleted successfully"));
        } catch (NumberFormatException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid ID format"));
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
}
