package com.dao;

import com.ntn.culinary.dao.UserDAO;

public class FakeUserDAO extends UserDAO {
    private final boolean exists;

    public FakeUserDAO(boolean exists) {
        this.exists = exists;
    }

    @Override
    public boolean existsById(int id) {
        return exists;
    }
}
