package com.ntn.culinary.dao;

import com.ntn.culinary.model.Category;

import java.util.List;

public interface CategoryDao {
    boolean existsByName(String name);

    void insertCategory(Category category);

    void updateCategory(Category category);

    List<Category> getAllCategories();

    Category getCategoryById(int id);

    void deleteCategoryById(int id);

    boolean existsById(int id);

    Category getCategoryByName(String name);
}
