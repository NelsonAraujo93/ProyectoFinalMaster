package com.example.servicesapi.repository;

import com.example.servicesapi.model.ServiceRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRequestRepository extends MongoRepository<ServiceRequest, String> {
    
    // Find all service requests by a specific client ID
    List<ServiceRequest> findByClientId(Integer clientId);
    
    // Find a service request by its ID and the client ID
    Optional<ServiceRequest> findByIdAndClientId(String id, Integer clientId);
    
    // Find a service request by service ID and client ID
    Optional<ServiceRequest> findByServiceIdAndClientId(Integer serviceId, Integer clientId);
    
    // Delete a service request by its ID and the client ID
    void deleteByIdAndClientId(String id, Integer clientId);
    List<ServiceRequest> findAllByServiceIdIn(List<Integer> serviceIds);
    Optional<ServiceRequest> findByServiceId(Integer serviceId);

    List<ServiceRequest> findTop3ByServiceIdAndStatusOrderByRatingDateDesc(Integer serviceId, String status);
}
