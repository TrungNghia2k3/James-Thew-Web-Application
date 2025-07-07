package com.ntn.culinary.dao;

import com.ntn.culinary.model.Category;

import java.util.List;

public interface CategoryDao {
    boolean existsByName(String name);

    List<Category> getAllCategories();

    Category getCategoryById(int id);
}
