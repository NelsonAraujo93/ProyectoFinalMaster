package com.example.securityservice.repository;

import com.example.securityservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    User findByUsername(String username);
}
