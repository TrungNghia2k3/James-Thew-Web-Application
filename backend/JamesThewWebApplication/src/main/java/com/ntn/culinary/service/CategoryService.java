package com.ntn.culinary.service;

import com.ntn.culinary.dao.CategoryDAO;
import com.ntn.culinary.model.Category;
import com.ntn.culinary.response.CategoryResponse;

import java.sql.SQLException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CategoryService {
    private static final CategoryService categoryService = new CategoryService();

    private CategoryService() {
        // Private constructor to prevent instantiation
    }

    public static CategoryService getInstance() {
        return categoryService;
    }

    private final CategoryDAO categoryDAO = CategoryDAO.getInstance();

    public boolean categoryExists(String name) throws Exception {
        return categoryDAO.existsByName(name);
    }

    public CategoryResponse getCategoryById(int id) throws SQLException {
        Category category = categoryDAO.getCategoryById(id);
        if (category == null) {
            return null;
        }
        return mapCategoryToResponse(category);
    }

    public List<CategoryResponse> getAllCategories() throws SQLException {
        return categoryDAO.getAllCategories().stream()
                .map(this::mapCategoryToResponse)
                .collect(toList());
    }

    private CategoryResponse mapCategoryToResponse(Category category)
    {
        String imageUrl = "http://localhost:8080/JamesThewWebApplication/api/images/categories/" + category.getPath();
        return new CategoryResponse(
            category.getId(),
            category.getName(),
            imageUrl
        );
    }
}
