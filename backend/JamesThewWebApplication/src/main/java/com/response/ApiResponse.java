package com.response;

import com.google.gson.annotations.Expose;

public class ApiResponse<T> {

    @Expose
    private boolean success;

    @Expose
    private int status;

    @Expose
    private String message;

    @Expose
    private T data;

    @Expose
    private String error;

    // Default constructor (required by Gson)
    public ApiResponse() {}

    // Success response
    public ApiResponse(int status, String message, T data) {
        this.success = true;
        this.status = status;
        this.message = message;
        this.data = data;
        this.error = null;
    }

    // Error response
    public ApiResponse(int status, String error) {
        this.success = false;
        this.status = status;
        this.message = null;
        this.data = null;
        this.error = error;
    }

    public ApiResponse(String message, Object error) {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {return message;}

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}