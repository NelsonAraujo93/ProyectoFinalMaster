package com.example.securityservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "user_roles")
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    // Default constructor
    public UserRole() {}

    // Constructor for easy creation
    public UserRole(User user, String role) {
        this.user = user;
        this.id = new UserRoleId(user.getId(), role);
    }

    // Getters and setters
    public UserRoleId getId() {
        return id;
    }

    public void setId(UserRoleId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (id == null) {
            id = new UserRoleId();
        }
        id.setUserId(user.getId());
    }

    public String getRole() {
        return id.getRole();
    }

    public void setRole(String role) {
        if (this.id == null) {
            this.id = new UserRoleId();
        }
        this.id.setRole(role);
    }
}