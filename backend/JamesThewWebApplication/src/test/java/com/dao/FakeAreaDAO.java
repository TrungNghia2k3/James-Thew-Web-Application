package com.dao;

public class FakeAreaDAO extends AreaDAO {
    private final boolean exists;

    public FakeAreaDAO(boolean exists) {
        this.exists = exists;
    }

    @Override
    public boolean existsByName(String name) {
        return exists;
    }
}
