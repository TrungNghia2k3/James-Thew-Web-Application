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

@WebServlet("/api/images/recipes/*")
public class ImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String filename = req.getPathInfo(); // /abc.jpg
        if (filename == null || filename.equals("/")) {
            ResponseUtil.sendResponse(resp, new ApiResponse<>(400, "Filename is required"));
            return;
        }

        filename = filename.substring(1); // remove "/"

        // Dẫn tới thư mục backend/images/recipes/
        File imageFile = new File("F:/Project/JamesThewWebApplication/JamesThewWebApplication/backend/images/recipes", filename);

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


