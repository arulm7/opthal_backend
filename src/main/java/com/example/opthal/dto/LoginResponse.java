package com.example.opthal.dto;

public class LoginResponse {

    private String message;
    private String role;
    private String name;

    public LoginResponse(String message, String role, String name) {
        this.message = message;
        this.role = role;
        this.name = name;
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
}