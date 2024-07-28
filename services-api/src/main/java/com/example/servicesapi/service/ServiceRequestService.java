package com.example.servicesapi.service;

import com.example.servicesapi.model.ServiceRequest;
import com.example.servicesapi.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceRequestService {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    public List<ServiceRequest> getAllServiceRequests() {
        return serviceRequestRepository.findAll();
    }

    public Optional<ServiceRequest> getServiceRequestById(Long id) {
        return serviceRequestRepository.findById(id);
    }

    public List<ServiceRequest> getAllServiceRequestsByClientId(Long clientId) {
        return serviceRequestRepository.findByClientId(clientId);
    }

    public Optional<ServiceRequest> getServiceRequestByIdAndClientId(String id, Long clientId) {
        return serviceRequestRepository.findByIdAndClientId(id, clientId);
    }

    public List<ServiceRequest> getServiceRequestsByServiceId(Long serviceId) {
        return serviceRequestRepository.findByServiceId(serviceId);
    }

    public Optional<ServiceRequest> getServiceRequestsByServiceIdAndClientId(Long clientId, Long serviceId) {
        return serviceRequestRepository.findByServiceIdAndClientId(serviceId, clientId);
    }

    public ServiceRequest createServiceRequest(ServiceRequest serviceRequest) {
        return serviceRequestRepository.save(serviceRequest);
    }

    public ServiceRequest updateServiceRequest(ServiceRequest serviceRequest) {
        return serviceRequestRepository.save(serviceRequest);
    }

    public void deleteServiceRequest(String id, Long clientId) {
        serviceRequestRepository.deleteByIdAndClientId(id, clientId);
    }
}
