package com.ntn.culinary.dao;

import com.ntn.culinary.model.Role;

import java.util.List;

public interface RoleDao {
    boolean existsByName(String name);

    boolean existsById(int id);

    List<Role> getAllRoles();

    Role getRoleById(int id);
}
