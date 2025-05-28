package com.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/protected/admin/contests")
public class ContestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        // Lấy danh sách roles từ request
        List<String> roles = (List<String>) req.getAttribute("roles");

        // Kiểm tra role admin
        if (roles == null || !roles.contains("ADMIN")) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"error\":\"Admin access required\"}");
            return;
        }

        // Trả về danh sách contest
        resp.getWriter().write("[{\"id\":1,\"title\":\"Best Vegetarian Recipe\",\"status\":\"active\"}]");
    }
}

