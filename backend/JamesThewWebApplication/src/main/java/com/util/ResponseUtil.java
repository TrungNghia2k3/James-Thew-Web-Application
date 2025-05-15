package com.util;

import com.google.gson.Gson;
import com.response.ApiResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {
    private static final Gson gson = new Gson();

    public static void sendResponse(HttpServletResponse resp, ApiResponse<?> response) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(response.getStatus());
        resp.getWriter().write(gson.toJson(response));
    }
}
