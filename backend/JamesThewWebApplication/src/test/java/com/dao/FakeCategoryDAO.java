package com.dao;

import com.ntn.culinary.dao.CategoryDAO;

public class FakeCategoryDAO extends CategoryDAO {
    private final boolean exists;

    public FakeCategoryDAO(boolean exists) {
        this.exists = exists;
    }

    @Override
    public boolean existsByName(String name) {
        return exists;
    }
}
