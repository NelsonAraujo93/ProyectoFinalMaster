package com.example.usersapi.repository;

import com.example.usersapi.model.UserRole;
import com.example.usersapi.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}
