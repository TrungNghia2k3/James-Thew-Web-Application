package com.dao;

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
