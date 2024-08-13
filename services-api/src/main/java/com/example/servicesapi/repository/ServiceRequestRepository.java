package com.example.servicesapi.repository;

import com.example.servicesapi.model.ServiceRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRequestRepository extends MongoRepository<ServiceRequest, String> {
    
    // Find all service requests by a specific client ID
    List<ServiceRequest> findByClientId(Long clientId);
    
    // Find a service request by its ID and the client ID
    Optional<ServiceRequest> findByIdAndClientId(String id, Long clientId);
    
    // Find a service request by service ID and client ID
    Optional<ServiceRequest> findByServiceIdAndClientId(Long serviceId, Long clientId);
    
    // Delete a service request by its ID and the client ID
    void deleteByIdAndClientId(String id, Long clientId);
    List<ServiceRequest> findAllByServiceIdIn(List<Long> serviceIds);
}
