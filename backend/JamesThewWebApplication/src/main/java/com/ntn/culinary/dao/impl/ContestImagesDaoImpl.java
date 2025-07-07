package com.ntn.culinary.dao.impl;

import com.ntn.culinary.dao.ContestImagesDao;
import com.ntn.culinary.model.ContestImages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.ntn.culinary.utils.DatabaseUtils.getConnection;

public class ContestImagesDaoImpl implements ContestImagesDao {

    public List<ContestImages> getContestImagesByContestId(int contestId) {

        String SELECT_CONTEST_BY_CONTEST_ID_QUERY = """
                SELECT * FROM contest_images WHERE contest_id = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_CONTEST_BY_CONTEST_ID_QUERY)) {

            stmt.setInt(1, contestId);

            List<ContestImages> images = new ArrayList<>();

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ContestImages image = new ContestImages();
                    image.setId(rs.getInt("id"));
                    image.setContestId(rs.getInt("contest_id"));
                    image.setImagePath(rs.getString("image_path"));
                    images.add(image);
                }
                return images;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("SQLException: " + ex.getMessage());
        }
    }
}
