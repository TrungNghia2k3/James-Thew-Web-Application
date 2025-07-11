package com.ntn.culinary.service;

import com.ntn.culinary.dao.RoleDao;
import com.ntn.culinary.dao.StaffPermissionsDao;
import com.ntn.culinary.dao.UserDao;
import com.ntn.culinary.exception.NotFoundException;

public class StaffPermissionsService {

    private final StaffPermissionsDao staffPermissionsDao;
    private final UserDao userDao;
    private final RoleDao roleDao;

    public StaffPermissionsService(StaffPermissionsDao staffPermissionsDao, UserDao userDao, RoleDao roleDao) {
        this.staffPermissionsDao = staffPermissionsDao;
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    public void assignPermissionToStaff(int userId, int roleId) {
        if (!userDao.existsById(userId)) {
            throw new NotFoundException("User does not exist");
        }
        if (!roleDao.existsById(roleId)) {
            throw new NotFoundException("Role does not exist");
        }
        staffPermissionsDao.assignPermissionToStaff(userId, roleId);
    }

    public void removePermissionFromStaff(int userId, int roleId) {
        if (!userDao.existsById(userId)) {
            throw new NotFoundException("User does not exist");
        }

        if (!roleDao.existsById(roleId)) {
            throw new NotFoundException("Role does not exist");
        }

        if (!staffPermissionsDao.existsStaffPermission(userId, roleId)) {
            throw new NotFoundException("Permission not assigned to user");
        }

        staffPermissionsDao.removePermissionFromStaff(userId, roleId);
    }
}
