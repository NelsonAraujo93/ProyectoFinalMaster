package com.example.servicesapi.service;

import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.model.ServiceRating;
import com.example.servicesapi.dto.ServiceRequestDTO;
import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.repository.ServiceRepository;
import com.example.servicesapi.repository.PymeRepository;
import com.example.servicesapi.repository.ServiceRatingRepository;

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

    @Autowired
    private ServiceRatingRepository serviceRatingRepository;

    @Transactional(readOnly = true)
    public List<ServiceModel> getAllServicesByUserId(Long userId) {
        Optional<Pyme> pyme = pymeRepository.findByUserId(userId);
        return pyme.map(serviceRepository::findAllByPyme).orElse(List.of());
    }

    @Transactional(readOnly = true)
    public Optional<ServiceModel> getServiceByIdAndUserId(Long id, Long userId) {
        Optional<Pyme> pyme = pymeRepository.findByUserId(userId);
        return pyme.flatMap(p -> serviceRepository.findByIdAndPyme(id, p));
    }

    @Transactional
    public ServiceModel createService(ServiceModel service) {
        return serviceRepository.save(service);
    }
    
    @Transactional
    public ServiceModel updateService(ServiceModel service, Long userId) {
        Pyme pyme = pymeRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Pyme not found"));
        service.setPyme(pyme);
        return serviceRepository.save(service);
    }

    @Transactional
    public void deleteService(Long id, Long userId) {
        Pyme pyme = pymeRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Pyme not found"));
        serviceRepository.deleteByIdAndPyme(id, pyme);
    }

    @Transactional
    public Optional<ServiceModel> getServiceById(Long id) {
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
                    .filter(service -> {
                        double averageRating = calculateAverageRating(service.getId());
                        return averageRating >= minRating;
                    })
                    .collect(Collectors.toList());
        }

        // Convert to DTOs
        return services.stream()
                .map(service -> new ServiceRequestDTO(
                        service.getId(),
                        service.getName(),
                        service.getDescription(),
                        service.getPrice(),
                        calculateAverageRating(service.getId())
                ))
                .collect(Collectors.toList());
    }

    private double calculateAverageRating(Long serviceId) {
        List<ServiceRating> ratings = serviceRatingRepository.findByServiceId(serviceId);
        return ratings.stream()
                .mapToDouble(ServiceRating::getRating)
                .average()
                .orElse(0.0);
    }
}
