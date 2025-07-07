package com.ntn.culinary.service;

import com.ntn.culinary.dao.CategoryDao;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.model.Category;
import com.ntn.culinary.response.CategoryResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class CategoryService {
    private final CategoryDao categoryDao;

    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public boolean categoryExists(String name) {
        return categoryDao.existsByName(name);
    }

    public CategoryResponse getCategoryById(int id) {
        Category category = categoryDao.getCategoryById(id);

        if (category == null) {
            throw new NotFoundException("Category with id " + id + " not found");
        }

        return mapCategoryToResponse(category);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryDao.getAllCategories().stream()
                .map(this::mapCategoryToResponse)
                .collect(toList());
    }

    private CategoryResponse mapCategoryToResponse(Category category) {
        String imageUrl = "http://localhost:8080/JamesThewWebApplication/api/images/categories/" + category.getPath();
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                imageUrl
        );
    }
}
