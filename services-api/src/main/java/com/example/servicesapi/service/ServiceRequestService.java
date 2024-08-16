package com.example.servicesapi.service;

import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.model.ServiceRequest;
import com.example.servicesapi.repository.PymeRepository;
import com.example.servicesapi.repository.ServiceRepository;
import com.example.servicesapi.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceRequestService {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private PymeRepository pymeRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    // Method to create a new service request
    public ServiceRequest createServiceRequest(ServiceRequest serviceRequest) {
        Optional<ServiceModel> serviceOptional = serviceRepository.findById(serviceRequest.getServiceId());
        if (serviceOptional.isEmpty()) {
            throw new IllegalArgumentException("Service not found.");
        }

        ServiceModel service = serviceOptional.get();

        Optional<Pyme> pymeOptional = pymeRepository.findByUserId(serviceRequest.getClientId());
        if (pymeOptional.isPresent() && pymeOptional.get().getId().equals(service.getPymeId())) {
            throw new IllegalArgumentException("Cannot create a service request for your own service.");
        }

        serviceRequest.setServiceName(service.getName());
        serviceRequest.setStatus("Pending");

        return serviceRequestRepository.save(serviceRequest);
    }

    // Method to update a service request
    public ServiceRequest updateServiceRequest(ServiceRequest serviceRequest) {
        return serviceRequestRepository.save(serviceRequest);
    }

    // Method to delete a service request by its ID and client ID
    public void deleteServiceRequest(String id, Integer clientId) {
        serviceRequestRepository.deleteByIdAndClientId(id, clientId);
    }

    // Method to retrieve service requests by client ID
    public List<ServiceRequest> getAllServiceRequestsByClientId(Integer clientId) {
        return serviceRequestRepository.findByClientId(clientId);
    }

    // Method to get a service request by its ID and client ID
    public Optional<ServiceRequest> getServiceRequestByIdAndClientId(String id, Integer clientId) {
        return serviceRequestRepository.findByIdAndClientId(id, clientId);
    }

    @Transactional
    public Optional<ServiceRequest> getServiceRequestById(String id) {
        return serviceRequestRepository.findById(id);
    }

    @Transactional
    public Map<String, List<ServiceRequest>> getServiceRequestsGroupedByStatus(Integer clientId) {
        List<ServiceRequest> requests = serviceRequestRepository.findByClientId(clientId);
        return requests.stream().collect(Collectors.groupingBy(ServiceRequest::getStatus));
    }

    @Transactional
    public ServiceRequest updateRequestToOnProcess(String requestId) {
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (!"Pending".equals(request.getStatus())) {
            throw new IllegalArgumentException("Request is not in Pending status");
        }

        request.setStatus("On Process");
        return serviceRequestRepository.save(request);
    }

    @Transactional
    public ServiceRequest updateRequestToComplete(String requestId) {
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (!"On Process".equals(request.getStatus())) {
            throw new IllegalArgumentException("Request is not in On Process status");
        }

        request.setStatus("Complete");
        return serviceRequestRepository.save(request);
    }

    @Transactional
    public ServiceRequest finalizeServiceRequest(String requestId, Integer rating, String comment) {
        // Fetch the service request
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Service Request not found"));

        // Ensure the request is in a state that can be finalized
        if (!"Complete".equals(request.getStatus())) {
            throw new IllegalStateException("Service request cannot be finalized");
        }

        // Update the request status, rating, and comment
        request.setStatus("Finalized");
        request.setRating(rating.doubleValue());
        request.setComment(comment);
        request.setRatingDate(new Date());

        // Update the service's rating and count
        ServiceModel service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));
        service.addRating(rating);  // This will also update the averageRating

        // Save the updated service and request
        serviceRepository.save(service);
        return serviceRequestRepository.save(request);
    }

    public ServiceRequest cancelServiceRequest(String requestId) {
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if ("Finished".equals(request.getStatus())) {
            throw new IllegalArgumentException("Cannot cancel a finished request");
        }

        request.setStatus("Canceled");
        return serviceRequestRepository.save(request);
    }

    public Map<String, List<ServiceRequest>> getServiceRequestsGroupedByStatusForPyme(Integer pymeUserId) {
        List<ServiceModel> services = serviceRepository.findAllByPymeId(pymeUserId);
        List<Integer> serviceIds = services.stream().map(ServiceModel::getId).collect(Collectors.toList());
        List<ServiceRequest> requests = serviceRequestRepository.findAllByServiceIdIn(serviceIds);

        return requests.stream().collect(Collectors.groupingBy(ServiceRequest::getStatus));
    }

    public ServiceRequest cancelRequestByPyme(String requestId, Integer pymeUserId) {
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (!"Pending".equals(request.getStatus())) {
            throw new IllegalArgumentException("Request is not in Pending status");
        }

        ServiceModel service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        if (!service.getPymeId().equals(pymeUserId)) {
            throw new IllegalArgumentException("Unauthorized access");
        }

        request.setStatus("Canceled");
        return serviceRequestRepository.save(request);
    }

    // Method to update a pending request to on-process by Pyme
    public ServiceRequest updateRequestToOnProcessByPyme(String requestId, Integer pymeUserId) {
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (!"Pending".equals(request.getStatus())) {
            throw new IllegalArgumentException("Request is not in Pending status");
        }

        ServiceModel service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        if (!service.getPymeId().equals(pymeUserId)) {
            throw new IllegalArgumentException("Unauthorized access");
        }

        request.setStatus("On Process");
        return serviceRequestRepository.save(request);
    }

    // Method to update an on-process request to complete by Pyme
    public ServiceRequest updateRequestToCompleteByPyme(String requestId, Integer pymeUserId) {
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (!"On Process".equals(request.getStatus())) {
            throw new IllegalArgumentException("Request is not in On Process status");
        }

        ServiceModel service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        if (!service.getPymeId().equals(pymeUserId)) {
            throw new IllegalArgumentException("Unauthorized access");
        }

        request.setStatus("Complete");
        return serviceRequestRepository.save(request);
    }
}

