package com.ntn.culinary.request;

public class UserRolesRequest {
    private int userId;
    private int roleId;

    public UserRolesRequest() {
    }

    public UserRolesRequest(int userId, int roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
