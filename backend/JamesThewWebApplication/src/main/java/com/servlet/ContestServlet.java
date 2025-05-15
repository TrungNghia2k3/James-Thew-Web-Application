package com.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/protected/admin/contests")
public class ContestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        // Get user details from JWT (set by JwtFilter)
        String role = (String) req.getAttribute("role");
        if (!"admin".equals(role)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"error\":\"Admin access required\"}");
            return;
        }

        // Fetch contests from database
        // Example response
        resp.getWriter().write("[{\"id\":1,\"title\":\"Best Vegetarian Recipe\",\"status\":\"active\"}]");
    }
}
