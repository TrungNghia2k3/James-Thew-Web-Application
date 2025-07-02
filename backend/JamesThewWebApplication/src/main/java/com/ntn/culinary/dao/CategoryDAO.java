package com.ntn.culinary.dao;

import com.ntn.culinary.model.Category;
import com.ntn.culinary.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    private static final CategoryDAO categoryDAO = new CategoryDAO();

    private CategoryDAO() {
        // Private constructor to prevent instantiation
    }

    public static CategoryDAO getInstance() {
        return categoryDAO;
    }

    private static final String CHECK_CATEGORY_EXISTS_BY_NAME_QUERY = "SELECT 1 FROM categories WHERE name = ? LIMIT 1";
    private static final String SELECT_ALL_CATEGORIES_QUERY = "SELECT * FROM categories";

    private static final String SELECT_CATEGORY_BY_ID_QUERY = "SELECT * FROM categories WHERE id = ?";

    public boolean existsByName(String name) throws SQLException {

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_CATEGORY_EXISTS_BY_NAME_QUERY)) {

            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true nếu tồn tại ít nhất 1 dòng
            }
        }
    }

    public List<Category> getAllCategories() throws SQLException {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_CATEGORIES_QUERY);
             ResultSet rs = stmt.executeQuery()) {

            List<Category> categories = new ArrayList<>();
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setPath(rs.getString("path"));
                categories.add(category);
            }
            return categories;
        }
    }

    public Category getCategoryById(int id) throws SQLException {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_CATEGORY_BY_ID_QUERY)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category();
                    category.setId(rs.getInt("id"));
                    category.setName(rs.getString("name"));
                    category.setPath(rs.getString("path"));
                    return category;
                } else {
                    return null; // Không tìm thấy category với ID này
                }
            }
        }
    }
}

