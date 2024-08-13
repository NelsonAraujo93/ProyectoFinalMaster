package com.example.servicesapi.repository;

import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.model.Pyme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceModel, Long> {
    List<ServiceModel> findAllByPyme(Pyme pyme);
    Optional<ServiceModel> findByIdAndPyme(Long id, Pyme pyme);
    void deleteByIdAndPyme(Long id, Pyme pyme);
    List<ServiceModel> findTop6ByOrderByIdDesc();
    List<ServiceModel> findAllByPyme_UserId(Long pymeUserId);
}
