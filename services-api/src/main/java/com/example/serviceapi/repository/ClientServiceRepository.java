package com.example.services.repository;

import com.example.services.model.ClientService;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClientServiceRepository extends MongoRepository<ClientService, String> {
    List<ClientService> findByServiceId(Long serviceId);
}
