package com.example.servicesapi.repository;

import com.example.servicesapi.model.ServiceRating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ServiceRatingRepository extends MongoRepository<ServiceRating, String> {
    List<ServiceRating> findByServiceId(Long serviceId);
    List<ServiceRating> findByClientId(Long clientId);
}
