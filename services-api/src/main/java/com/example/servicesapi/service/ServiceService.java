package com.example.servicesapi.service;

import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<ServiceModel> getAllServicesByUserId(Long userId) {
        return serviceRepository.findAllByUserId(userId);
    }

    public Optional<ServiceModel> getServiceByIdAndUserId(Long id, Long userId) {
        return serviceRepository.findByIdAndUserId(id, userId);
    }

    public ServiceModel createService(ServiceModel service) {
        return serviceRepository.save(service);
    }

    public ServiceModel updateService(ServiceModel service) {
        return serviceRepository.save(service);
    }

    public void deleteService(Long id, Long userId) {
        serviceRepository.deleteByIdAndUserId(id, userId);
    }
}
