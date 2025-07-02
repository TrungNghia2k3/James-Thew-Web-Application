package com.ntn.culinary.service;

import com.ntn.culinary.dao.RoleDAO;
import com.ntn.culinary.dao.StaffPermissionsDAO;
import com.ntn.culinary.dao.UserDAO;

public class StaffPermissionsService {
    private static final StaffPermissionsService staffPermissonsService = new StaffPermissionsService();

    private StaffPermissionsService() {
        // Private constructor to prevent instantiation
    }

    public static StaffPermissionsService getInstance() {
        return staffPermissonsService;
    }

    private final StaffPermissionsDAO staffPermissionsDAO = StaffPermissionsDAO.getInstance();
    private final UserDAO userDAO = UserDAO.getInstance();
    private final RoleDAO roleDAO = RoleDAO.getInstance();

    public void assignPermissionToStaff(int userId, int roleId) throws Exception {
        if (!userDAO.existsById(userId)) {
            throw new Exception("User does not exist");
        }
        if (!roleDAO.existsById(roleId)) {
            throw new Exception("Role does not exist");
        }
        staffPermissionsDAO.assignPermissionToStaff(userId, roleId);
    }

    public void removePermissionFromStaff(int userId, int roleId) throws Exception {
        if (!userDAO.existsById(userId)) {
            throw new Exception("User does not exist");
        }
        if (!roleDAO.existsById(roleId)) {
            throw new Exception("Role does not exist");
        }
        staffPermissionsDAO.removePermissionFromStaff(userId, roleId);
    }
}
