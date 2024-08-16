package com.example.servicesapi.service;

import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.dto.ServiceRequestDTO;
import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.repository.ServiceRepository;
import com.example.servicesapi.repository.PymeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private PymeRepository pymeRepository;

    @Transactional(readOnly = true)
    public List<ServiceModel> getAllServicesByUserId(Integer userId) {
        Optional<Pyme> pyme = pymeRepository.findByUserId(userId);
        return pyme.map(p -> serviceRepository.findAllByPymeId(p.getId())).orElse(List.of());
    }

    @Transactional(readOnly = true)
    public Optional<ServiceModel> getServiceByIdAndUserId(Integer id, Integer userId) {
        Optional<Pyme> pyme = pymeRepository.findByUserId(userId);
        return pyme.flatMap(p -> serviceRepository.findByIdAndPymeId(id, p.getId()));
    }

    @Transactional
    public ServiceModel createService(ServiceModel service) {
        return serviceRepository.save(service);
    }

    @Transactional
    public ServiceModel updateService(ServiceModel service, Integer userId) {
        Pyme pyme = pymeRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Pyme not found"));
        service.setPymeId(pyme.getId());
        return serviceRepository.save(service);
    }

    @Transactional
    public void deleteService(Integer id, Integer userId) {
        serviceRepository.deleteByIdAndPymeId(id, userId);
    }

    @Transactional
    public Optional<ServiceModel> getServiceById(Integer id) {
        return serviceRepository.findById(id);
    }

    @Transactional
    public List<ServiceRequestDTO> filterServices(Double minPrice, Double maxPrice, Double minRating, String name) {
        List<ServiceModel> services = serviceRepository.findAll();

        // Filter by price range
        if (minPrice != null) {
            services = services.stream()
                    .filter(service -> service.getPrice() >= minPrice)
                    .collect(Collectors.toList());
        }
        if (maxPrice != null) {
            services = services.stream()
                    .filter(service -> service.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }

        // Filter by name
        if (name != null && !name.isEmpty()) {
            services = services.stream()
                    .filter(service -> service.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Filter by rating
        if (minRating != null) {
            services = services.stream()
                    .filter(service -> service.getAverageRating() >= minRating)
                    .collect(Collectors.toList());
        }

        // Convert to DTOs
        return services.stream()
                .map(service -> new ServiceRequestDTO(
                        service.getId(),
                        service.getName(),
                        service.getDescription(),
                        service.getPrice(),
                        service.getAverageRating() // Use the pre-calculated average rating
                ))
                .collect(Collectors.toList());
    }

    public ServiceModel addRatingToService(Integer serviceId, double rating) {
        ServiceModel service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));
    
        service.addRating(rating);
        return serviceRepository.save(service);
    }
    
    public ServiceModel updateRatingForService(Integer serviceId, double oldRating, double newRating) {
        ServiceModel service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));
    
        service.updateRating(oldRating, newRating);
        return serviceRepository.save(service);
    }
}
