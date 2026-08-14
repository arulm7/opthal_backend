package com.example.opthal.dto;

public class LoginResponse {

    private String message;
    private String role;
    private String name;
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(
            String message,
            String role,
            String name,
            String token) {

        this.message = message;
        this.role = role;
        this.name = name;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }
}