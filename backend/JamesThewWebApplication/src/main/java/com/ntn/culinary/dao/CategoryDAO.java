package com.ntn.culinary.dao;

import com.ntn.culinary.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDAO {
    private static final String CHECK_CATEGORY_EXISTS_BY_NAME_QUERY = "SELECT 1 FROM categories WHERE name = ? LIMIT 1";

    public boolean existsByName(String name) throws SQLException {

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_CATEGORY_EXISTS_BY_NAME_QUERY)) {

            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true nếu tồn tại ít nhất 1 dòng
            }
        }
    }
}

