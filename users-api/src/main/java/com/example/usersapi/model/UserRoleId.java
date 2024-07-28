package com.example.usersapi.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserRoleId implements Serializable {

    private Long userId;
    private String role;

    // Default constructor
    public UserRoleId() {}

    // Constructor for easy creation
    public UserRoleId(Long userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleId that = (UserRoleId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, role);
    }
}