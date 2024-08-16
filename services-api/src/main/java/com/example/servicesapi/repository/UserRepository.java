package com.example.servicesapi.repository;

import com.example.servicesapi.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRolesContaining(String role);
}
