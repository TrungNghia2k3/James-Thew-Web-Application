package com.ntn.culinary.request;

import com.google.gson.annotations.Expose;

public class AreaRequest {
    @Expose
    private String name;

    public AreaRequest() {}

    public AreaRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
