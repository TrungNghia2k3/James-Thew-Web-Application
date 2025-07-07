package com.ntn.culinary.servlet.admin;

import com.ntn.culinary.dao.AreaDao;
import com.ntn.culinary.dao.impl.AreaDaoImpl;
import com.ntn.culinary.request.AreaRequest;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.service.AreaService;
import com.ntn.culinary.utils.GsonUtils;
import com.ntn.culinary.utils.ValidationUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static com.ntn.culinary.utils.CastUtils.toStringList;
import static com.ntn.culinary.utils.ResponseUtils.sendResponse;

@WebServlet("/api/protected/admin/areas")
public class AreaServlet extends HttpServlet {

    private final AreaService areaService;

    public AreaServlet() {
        // Inject AreaDaoImpl
        AreaDao areaDao = new AreaDaoImpl();
        this.areaService = new AreaService(areaDao);
    }

    // Có nghĩa:
    // Tự chịu trách nhiệm tạo dependency (AreaDaoImpl, AreaService)
    // Servlet container (Tomcat) chỉ cần gọi constructor mặc định không tham số (), nó biết cách khởi tạo servlet.
    // Đây là cách truyền thống, hoạt động ngay lập tức mà không cần framework hỗ trợ Dependency Injection.

    //  Nếu viết constructor có tham số thì sao?
    //Vấn đề ở đây:
    //Tomcat KHÔNG biết làm sao để cung cấp AreaService vào tham số constructor.
    //Servlet container chỉ hỗ trợ constructor mặc định không tham số khi tự động khởi tạo servlet.
    //Nó không tự inject dependency qua constructor như Spring.

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        // Lấy thông tin từ JwtFilter
        List<String> roles = toStringList(req.getAttribute("roles"));

        if (roles == null || !roles.contains("ADMIN")) {
            sendResponse(resp, new ApiResponse<>(403, "Access denied: ADMIN role required"));
            return;
        }

        // Lấy thông tin từ JwtFilter
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        // Lấy thông tin từ JwtFilter
        List<String> roles = toStringList(req.getAttribute("roles"));

        if (roles == null || !roles.contains("ADMIN")) {
            sendResponse(resp, new ApiResponse<>(403, "Access denied: ADMIN role required"));
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
            sendResponse(resp, new ApiResponse<>(400, "Invalid request payload"));
            return;
        }

        // Parse JSON
        AreaRequest areaRequest = GsonUtils.fromJson(sb.toString(), AreaRequest.class);

        // Validate input
        if (ValidationUtils.isNullOrEmpty(areaRequest.getName())) {
            sendResponse(resp, new ApiResponse<>(400, "Area name is required"));
            return;
        }

        try {
            areaService.addArea(areaRequest.getName());
            sendResponse(resp, new ApiResponse<>(200, "Area added successfully"));
        } catch (Exception e) {
            sendResponse(resp, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    private void handleGetById(String idParam, HttpServletResponse resp) {
        try {
            int id = Integer.parseInt(idParam);
            var area = areaService.getAreaById(id);

            if (area != null) {
                sendResponse(resp, new ApiResponse<>(200, "Area fetched successfully", area));
            } else {
                sendResponse(resp, new ApiResponse<>(404, "Area with ID " + id + " does not exist"));
            }
        } catch (NumberFormatException e) {
            sendResponse(resp, new ApiResponse<>(400, "Invalid ID format"));
        }
    }

    private void handleGetAll(HttpServletResponse resp){
        try {
            var areas = areaService.getAllAreas();
            sendResponse(resp, new ApiResponse<>(200, "All areas fetched", areas));
        } catch (RuntimeException e) {
            sendResponse(resp, new ApiResponse<>(500, "Database error: " + e.getMessage()));
        }
    }
}
