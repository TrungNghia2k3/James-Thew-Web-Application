package com.ntn.culinary.servlet.staff;

import com.ntn.culinary.constant.PermissionType;
import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.utils.CastUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.ntn.culinary.utils.ResponseUtils.sendResponse;

@WebServlet("/api/protected/staff/secure-resource-2")
public class StaffOnlyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        // Truy cập thành công
        sendResponse(resp, new ApiResponse<>(200, "OK"));
    }
}

