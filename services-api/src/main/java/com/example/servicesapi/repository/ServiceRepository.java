package com.example.servicesapi.repository;

import com.example.servicesapi.model.ServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceModel, Integer> {
    List<ServiceModel> findAllByPymeId(Integer pymeId);
    Optional<ServiceModel> findByIdAndPymeId(Integer id, Integer pymeId);
    void deleteByIdAndPymeId(Integer id, Integer pymeId);
    List<ServiceModel> findTop6ByOrderByIdDesc();
}
