package com.ntn.culinary.request;

public class StaffPermissionsRequest {
    private int userId;
    private int roleId;

    public StaffPermissionsRequest() {
    }

    public StaffPermissionsRequest(int userId, int roleId) {
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
