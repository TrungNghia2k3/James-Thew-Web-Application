package com.ntn.culinary.service;

import com.ntn.culinary.dao.RoleDAO;
import com.ntn.culinary.dao.UserDAO;
import com.ntn.culinary.dao.UserRolesDAO;

public class UserRolesService {
    private static final UserRolesService userRolesService = new UserRolesService();

    private UserRolesService() {
        // Private constructor to prevent instantiation
    }

    public static UserRolesService getInstance() {
        return userRolesService;
    }

    private final UserRolesDAO userRolesDAO = UserRolesDAO.getInstance();
    private final UserDAO userDAO = UserDAO.getInstance();
    private final RoleDAO roleDAO = RoleDAO.getInstance();

    public void assignRoleToUser(int userId, int roleId) throws Exception {
        if (!userDAO.existsById(userId)) {
            throw new Exception("User does not exist");
        }
        if (!roleDAO.existsById(roleId)) {
            throw new Exception("Role does not exist");
        }
        userRolesDAO.assignRoleToUser(userId, roleId);
    }

    public void removeRoleFromUser(int userId, int roleId) throws Exception {
        if (!userDAO.existsById(userId)) {
            throw new Exception("User does not exist");
        }
        if (!roleDAO.existsById(roleId)) {
            throw new Exception("Role does not exist");
        }
        userRolesDAO.removeRoleFromUser(userId, roleId);
    }
}
