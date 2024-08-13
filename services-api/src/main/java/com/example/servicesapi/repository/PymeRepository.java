package com.example.servicesapi.repository;

import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PymeRepository extends JpaRepository<Pyme, Long> {
    
    // Method to find a Pyme by its associated user's ID
    Optional<Pyme> findByUserId(Long userId);
    Optional<Pyme> findByUser(User user);
    List<Pyme> findTop6ByOrderByIdDesc();

    // Method to delete a Pyme by its ID (with cascading delete for associated services)
    void deleteById(@SuppressWarnings("null") Long id);
}
