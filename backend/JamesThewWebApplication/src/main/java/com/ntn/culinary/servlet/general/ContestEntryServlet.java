package com.ntn.culinary.servlet.general;

import com.google.gson.reflect.TypeToken;
import com.ntn.culinary.model.ContestEntryInstruction;
import com.ntn.culinary.request.ContestEntryRequest;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.service.ContestEntryService;
import com.ntn.culinary.utils.CastUtils;
import com.ntn.culinary.utils.GsonUtils;
import com.ntn.culinary.utils.ResponseUtils;
import com.ntn.culinary.utils.ValidationUtils;

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

@WebServlet("/api/protected/general/contest-entry")
@MultipartConfig
public class ContestEntryServlet extends HttpServlet {
    private final ContestEntryService contestEntryService = ContestEntryService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            req.setCharacterEncoding("UTF-8");

            // Lấy thông tin từ JwtFilter
            List<String> roles = CastUtils.toStringList(req.getAttribute("roles"));

            if (roles == null || !roles.contains("GENERAL")) {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(403, "Access denied: GENERAL role required"));
                return;
            }

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
            Map<String, String> errors = validateContestEntryRequest(contestEntryRequest, imagePart);

            if (!errors.isEmpty()) {
                ResponseUtils.sendResponse(resp, new ApiResponse<>(400, "Validation errors", errors));
                return;
            }

            contestEntryService.addContestEntry(contestEntryRequest, imagePart);
            ResponseUtils.sendResponse(resp, new ApiResponse<>(200, "Contest entry created successfully", null));

        } catch (Exception e) {
            ResponseUtils.sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    private Map<String, String> validateContestEntryRequest(ContestEntryRequest request, Part imagePart) {

        Map<String, String> errors = new HashMap<>();

        if (ValidationUtils.isNotExistId(request.getContestId())) {
            errors.put("contestId", "Contest ID is required");
        }
        if (ValidationUtils.isNotExistId(request.getUserId())) {
            errors.put("userId", "User ID is required");
        }

        if (ValidationUtils.isNullOrEmpty(request.getName())) {
            errors.put("name", "Name is required");
        }

        if (ValidationUtils.isNullOrEmpty(request.getIngredients())) {
            errors.put("ingredients", "Ingredients is required");
        }

        // Image
        if (imagePart == null || imagePart.getSize() == 0) {
            errors.put("image", "Image is required");
        }

        if (ValidationUtils.isNullOrEmpty(request.getPrepareTime())) {
            errors.put("prepareTime", "Prepare time is required");
        }

        if (ValidationUtils.isNullOrEmpty(request.getCookingTime())) {
            errors.put("cookingTime", "Cooking time is required");
        }

        if (ValidationUtils.isNullOrEmpty(request.getYield())) {
            errors.put("yield", "Yield is required");
        }

        if (ValidationUtils.isNullOrEmpty(request.getCategory())) {
            errors.put("category", "Category is required");
        }

        if (ValidationUtils.isNullOrEmpty(request.getArea())) {
            errors.put("area", "Area is required");
        }

        if (ValidationUtils.isNullOrEmpty(request.getShortDescription())) {
            errors.put("shortDescription", "Short description is required");
        }

        if (request.getContestEntryInstructions() == null || request.getContestEntryInstructions().isEmpty()) {
            errors.put("instructions", "Instructions are required");
        } else {
            for (int i = 0; i < request.getContestEntryInstructions().size(); i++) {
                ContestEntryInstruction instruction = request.getContestEntryInstructions().get(i);

                if (instruction.getStepNumber() <= 0) {
                    errors.put("instruction[" + i + "].stepNumber", "Step number is required");
                }

                if (ValidationUtils.isNullOrEmpty(instruction.getName())) {
                    errors.put("instruction[" + i + "].name", "Name is required");
                }

                if (ValidationUtils.isNullOrEmpty(instruction.getText())) {
                    errors.put("instruction[" + i + "].text", "Instruction text is required");
                }
            }
        }

        return errors;
    }
}
