package com.ntn.culinary.request;

public class RoleRequest {
    private int id;
    private String name;

    public RoleRequest() {
    }

    public RoleRequest(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
