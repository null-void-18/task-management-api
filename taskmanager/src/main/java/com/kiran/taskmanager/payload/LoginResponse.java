package com.kiran.taskmanager.payload;

public class LoginResponse {
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(String token) {
        this.token = token;
    }

    // Getter and setter
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
