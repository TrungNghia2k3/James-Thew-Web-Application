package com.request;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class UserRequest {
    @Expose
    private int id;

    @Expose
    private String username;

    @Expose
    private String password;

    @Expose
    private String email;

    @Expose
    private String firstName;

    @Expose
    private String lastName;

    @Expose
    private String phone;

    public UserRequest() {
    }

    public UserRequest(int id, String username, String password, String email, String firstName, String lastName, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
