package com.ntn.culinary.utils;

import com.google.gson.Gson;
import com.ntn.culinary.response.ApiResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtils {
    private static final Gson gson = new Gson();

    public static void sendResponse(HttpServletResponse resp, ApiResponse<?> response) {
        resp.setContentType("application/json");
        resp.setStatus(response.getStatus());
        try {
            resp.getWriter().write(gson.toJson(response));
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred while writing response", e);
        }

    }
}
