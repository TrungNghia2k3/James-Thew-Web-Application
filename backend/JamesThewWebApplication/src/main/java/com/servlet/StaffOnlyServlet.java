package com.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/protected/staff/secure-resource")
public class StaffOnlyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        // Lấy thông tin từ JwtFilter
        List<String> roles = (List<String>) req.getAttribute("roles");
        List<String> permissions = (List<String>) req.getAttribute("permissions");

        // Kiểm tra role
        if (roles == null || !roles.contains("STAFF")) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"error\":\"Access denied: STAFF role required\"}");
            return;
        }

        // Kiểm tra permission cụ thể
        if (permissions == null || !permissions.contains("ANSWER_QUESTIONS")) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"error\":\"Access denied: ANSWER_QUESTIONS permission required\"}");
            return;
        }

        // Truy cập thành công
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("{\"message\":\"Welcome, STAFF with permission!\"}");
    }
}

