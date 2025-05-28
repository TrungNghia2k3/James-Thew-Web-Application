package com.util;

import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

public class ImageUtil {

    /**
     * Lưu file ảnh upload vào thư mục /images/recipes của webapp
     * @param imagePart Part lấy từ request.getPart("image")
     * @param servletContext để lấy đường dẫn tuyệt đối
     * @param baseFileName Chuỗi base để tạo tên file (vd: slug của tên recipe)
     * @return tên file đã lưu (vd: apple-frangipan-1689012345678.jpg)
     * @throws IOException
     */
    // Chỉ định đường dẫn tuyệt đối đến thư mục chứa ảnh
    private static final String IMAGE_DIRECTORY = "F:/Project/JamesThewWebApplication/JamesThewWebApplication/backend/images/recipes";

    public static String saveImage(Part imagePart, String baseFileName) throws IOException {
        if (imagePart == null || imagePart.getSize() == 0) {
            return null;
        }

        // Tạo thư mục nếu chưa tồn tại
        File uploadDir = new File(IMAGE_DIRECTORY);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Lấy phần mở rộng của ảnh
        String submittedFileName = imagePart.getSubmittedFileName();
        String ext = "";
        int i = submittedFileName.lastIndexOf('.');
        if (i > 0) {
            ext = submittedFileName.substring(i);
        }

        // Tạo tên file mới
        String filename = baseFileName + "-" + System.currentTimeMillis() + ext;
        File file = new File(uploadDir, filename);

        // Ghi file
        imagePart.write(file.getAbsolutePath());

        return filename;
    }
    /**
     * Hàm slugify đơn giản chuyển chuỗi thành dạng url-safe
     */
    public static String slugify(String input) {
        if (input == null) return "";
        return input.toLowerCase()
                .replaceAll("[^\\w\\s-]", "")
                .replaceAll("\\s+", "-")
                .trim();
    }
}
