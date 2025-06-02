package com.servlet;

import com.response.ApiResponse;
import com.util.ResponseUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/api/images/*")
public class ImageServlet extends HttpServlet {

    private static final String BASE_PATH = "F:/Project/JamesThewWebApplication/JamesThewWebApplication/backend/images/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Lấy phần path sau /api/images/
        String pathInfo = req.getPathInfo(); // Ví dụ: /recipes/abc.jpg hoặc /avatars/def.png

        if (pathInfo == null || pathInfo.equals("/")) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(400, "Path is required"));
            return;
        }

        // Tách phần thư mục (recipes, avatars, ...) và tên file
        String[] parts = pathInfo.split("/", 3); // [ "", "recipes", "abc.jpg" ]

        if (parts.length < 3) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(400, "Invalid path. Must include type and filename"));
            return;
        }

        String type = parts[1]; // recipes hoặc avatars
        String filename = parts[2]; // abc.jpg

        // Xác định thư mục tương ứng
        File imageFile = new File(BASE_PATH + type, filename);

        if (!imageFile.exists()) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(404, "Image not found"));
            return;
        }

        String mimeType = getServletContext().getMimeType(imageFile.getName());
        if (mimeType == null) mimeType = "application/octet-stream";

        resp.setContentType(mimeType);
        resp.setContentLengthLong(imageFile.length());

        try (FileInputStream in = new FileInputStream(imageFile);
             OutputStream out = resp.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}



