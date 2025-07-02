package com.ntn.culinary.dao;

import com.ntn.culinary.model.ContestImages;
import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.SQLException;
import java.util.List;

public class ContestImagesDAO {
    private static final ContestImagesDAO contestImagesDAO = new ContestImagesDAO();

    private ContestImagesDAO() {
        // Private constructor to prevent instantiation
    }

    public static ContestImagesDAO getInstance() {
        return contestImagesDAO;
    }

    private static final String SELECT_CONTEST_BY_CONTEST_ID_QUERY = """
            SELECT * FROM contest_images WHERE contest_id = ?
            """;

    public List<ContestImages> getContestImagesByContestId(int contestId) throws SQLException {
        try (var conn = DatabaseUtils.getConnection();
             var stmt = conn.prepareStatement(SELECT_CONTEST_BY_CONTEST_ID_QUERY)) {

            stmt.setInt(1, contestId);
            try (var rs = stmt.executeQuery()) {
                List<ContestImages> images = new java.util.ArrayList<>();
                while (rs.next()) {
                    ContestImages image = new ContestImages();
                    image.setId(rs.getInt("id"));
                    image.setContestId(rs.getInt("contest_id"));
                    image.setImagePath(rs.getString("image_path"));
                    images.add(image);
                }
                return images;
            }
        }
    }
}
