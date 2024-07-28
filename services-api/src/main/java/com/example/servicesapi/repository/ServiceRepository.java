package com.example.servicesapi.repository;

import com.example.servicesapi.model.ServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceModel, Long> {
    List<ServiceModel> findAllByUserId(Long userId);

    Optional<ServiceModel> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);
}
