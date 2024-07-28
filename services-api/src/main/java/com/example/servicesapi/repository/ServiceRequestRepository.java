package com.example.servicesapi.repository;

import com.example.servicesapi.model.ServiceRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRequestRepository extends MongoRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByClientId(Long clientId);
    List<ServiceRequest> findByServiceId(Long serviceId);
    Optional<ServiceRequest> findByIdAndClientId(String id, Long clientId);
    Optional<ServiceRequest> findByServiceIdAndClientId(Long serviceId, Long clientId);
    void deleteByIdAndClientId(String id, Long clientId);
}
