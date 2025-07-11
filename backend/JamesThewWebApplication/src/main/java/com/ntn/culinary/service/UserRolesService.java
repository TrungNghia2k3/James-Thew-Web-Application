package com.ntn.culinary.service;

import com.ntn.culinary.dao.RoleDao;
import com.ntn.culinary.dao.UserDao;
import com.ntn.culinary.dao.UserRolesDao;
import com.ntn.culinary.exception.NotFoundException;

public class UserRolesService {

    private final UserRolesDao userRolesDao;
    private final UserDao userDao;
    private final RoleDao roleDao;

    public UserRolesService(UserRolesDao userRolesDao, UserDao userDao, RoleDao roleDao) {
        this.userRolesDao = userRolesDao;
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    public void assignRoleToUser(int userId, int roleId) {
        if (!userDao.existsById(userId)) {
            throw new NotFoundException("User does not exist");
        }
        if (!roleDao.existsById(roleId)) {
            throw new NotFoundException("Role does not exist");
        }
        userRolesDao.assignRoleToUser(userId, roleId);
    }

    public void removeRoleFromUser(int userId, int roleId) {
        if (!userDao.existsById(userId)) {
            throw new NotFoundException("User does not exist");
        }
        if (!roleDao.existsById(roleId)) {
            throw new NotFoundException("Role does not exist");
        }

        if (!userRolesDao.existsUserRole(userId, roleId)) {
            throw new NotFoundException("Role not assigned to user");
        }

        userRolesDao.removeRoleFromUser(userId, roleId);
    }
}
