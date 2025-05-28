package com.model;

public class StaffPermission {
    private int userId;
    private int permissionId;

    public StaffPermission() {}

    public StaffPermission(int userId, int permissionId) {
        this.userId = userId;
        this.permissionId = permissionId;
    }

    public int getUserId() {return userId;}

    public void setUserId(int userId) {this.userId = userId;}

    public int getPermissionId() {return permissionId;}

    public void setPermissionId(int permissionId) {this.permissionId = permissionId;}
}
