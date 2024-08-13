package com.example.servicesapi.service;

import com.example.servicesapi.model.ServiceRating;
import com.example.servicesapi.repository.ServiceRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRatingService {

    @Autowired
    private ServiceRatingRepository serviceRatingRepository;

    public ServiceRating createServiceRating(ServiceRating serviceRating) {
        return serviceRatingRepository.save(serviceRating);
    }

    public List<ServiceRating> getAllRatingsByServiceId(Long serviceId) {
        return serviceRatingRepository.findByServiceId(serviceId);
    }

    public List<ServiceRating> getAllRatingsByClientId(Long clientId) {
        return serviceRatingRepository.findByClientId(clientId);
    }
}
