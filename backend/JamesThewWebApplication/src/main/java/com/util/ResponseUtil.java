package com.util;

import com.google.gson.Gson;
import com.response.ApiResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {

    private final Gson gson = new Gson();

    public void sendResponse(HttpServletResponse response, ApiResponse apiResponse, int statusCode) throws IOException {
        String json = gson.toJson(apiResponse);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
        response.setStatus(statusCode);
    }

    public void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        sendResponse(response, new ApiResponse(message, null), 500);
    }

    public void sendSuccessResponse(HttpServletResponse response, String message, Object result) throws IOException {
        sendResponse(response, new ApiResponse(message, result), 200);
    }


    public void sendNotFoundResponse(HttpServletResponse response, String message) throws IOException {
        sendResponse(response, new ApiResponse(message, null), 404);
    }

    public void sendBadRequestResponse(HttpServletResponse response, String message) throws IOException {
        sendResponse(response, new ApiResponse(message, null), 400);
    }
}
