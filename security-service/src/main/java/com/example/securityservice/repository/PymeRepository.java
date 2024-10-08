package com.example.securityservice.repository;

import com.example.securityservice.model.Pyme;
import com.example.securityservice.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PymeRepository extends JpaRepository<Pyme, Long> {
     
    // Method to find a Pyme by its associated user's ID
    Optional<Pyme> findByUserId(Long userId);
    Optional<Pyme> findByUser(User user);

    // Method to delete a Pyme by its ID (with cascading delete for associated services)
    void deleteById(@SuppressWarnings("null") Long id);
}
