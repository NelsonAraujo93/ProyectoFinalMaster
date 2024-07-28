package com.example.securityservice.model;

import java.util.List;

public class AuthResponse {
    private Long userId;
    private String username;
    private List<String> roles;
    private boolean isTokenBlacklisted;

    public AuthResponse(Long userId, String username, List<String> roles, boolean isTokenBlacklisted) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
        this.isTokenBlacklisted = isTokenBlacklisted;
    }

    public AuthResponse(String message, boolean isTokenBlacklisted) {
        this.username = message;
        this.isTokenBlacklisted = isTokenBlacklisted;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isTokenBlacklisted() {
        return isTokenBlacklisted;
    }

    public void setTokenBlacklisted(boolean tokenBlacklisted) {
        isTokenBlacklisted = tokenBlacklisted;
    }
}
