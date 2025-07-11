package com.ntn.culinary.service;

import com.ntn.culinary.dao.RoleDao;
import com.ntn.culinary.dao.UserRolesDao;
import com.ntn.culinary.exception.ConflictException;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.model.Role;
import com.ntn.culinary.request.RoleRequest;
import com.ntn.culinary.response.RoleResponse;

import java.util.List;

public class RoleService {
    private final RoleDao roleDao;
    private final UserRolesDao userRolesDao;

    public RoleService(RoleDao roleDao, UserRolesDao userRolesDao) {
        this.roleDao = roleDao;
        this.userRolesDao = userRolesDao;
    }

    public void addRole(String name) {
        if (!roleDao.existsByName(name)) {
            roleDao.addRole(name.toUpperCase());
        } else {
            throw new ConflictException("Role with name '" + name + "' already exists.");
        }
    }

    public void updateRole(RoleRequest roleRequest) {
        if (roleDao.existsById(roleRequest.getId())) {
            if (!roleDao.getRoleById(roleRequest.getId()).getName().equalsIgnoreCase(roleRequest.getName())
                && roleDao.existsByName(roleRequest.getName())) {
                throw new ConflictException("Role with name '" + roleRequest.getName() + "' already exists.");
            }

            Role role = new Role();
            role.setId(roleRequest.getId());
            role.setName(roleRequest.getName().toUpperCase());

            roleDao.updateRole(role);
        } else {
            throw new NotFoundException("Role with ID " + roleRequest.getId() + " does not exist.");
        }
    }

    public RoleResponse getRoleById(int id) {
        if (roleDao.existsById(id)) {
            return mapRoleToResponse(roleDao.getRoleById(id));
        } else {
            throw new NotFoundException("Role with ID " + id + " does not exist.");
        }
    }

    public void deleteRole(int id) {

        // Check if the role is assigned to any users
        if (userRolesDao.existsRoleId(id)) {
            throw new ConflictException("Role with cannot be deleted because it is assigned to users.");
        }

        // Check if the role exists before attempting to delete
        if (roleDao.existsById(id)) {
            roleDao.deleteRoleById(id);
        } else {
            throw new NotFoundException("Role does not exist.");
        }
    }

    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleDao.getAllRoles();
        if (roles.isEmpty()) {
            throw new NotFoundException("No roles found.");
        }
        return roles.stream()
                .map(this::mapRoleToResponse)
                .toList();
    }

    public RoleResponse getRoleByName(String name) {
        Role role = roleDao.getRoleByName(name.toUpperCase());
        if (role != null) {
            return mapRoleToResponse(role);
        } else {
            throw new NotFoundException("Role with name '" + name + "' does not exist.");
        }
    }

    private RoleResponse mapRoleToResponse(Role role) {
        return new RoleResponse(role.getId(), role.getName());
    }
}
