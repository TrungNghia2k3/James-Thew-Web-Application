package com.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/contests/submit")
public class ContestSubmissionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String content = req.getParameter("content");
        int contestId = Integer.parseInt(req.getParameter("contest_id"));

        // Save submission to database (user_id = NULL for non-registered)
        // Example response
        resp.getWriter().write("{\"message\":\"Submission received\"}");
    }
}
