package com.ntn.culinary.dao;

import com.ntn.culinary.model.Permission;

import java.util.List;

public interface PermissionDao {
    boolean existsByName(String name);

    List<Permission> getAllPermissions();

    Permission getPermissionById(int id);
}
