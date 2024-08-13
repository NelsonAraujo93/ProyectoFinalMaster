package com.example.servicesapi.service;

import com.example.servicesapi.dto.ServiceRatingDTO;
import com.example.servicesapi.dto.ServiceRequestDetailedDTO;
import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.model.RequestFinished;
import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.model.ServiceRating;
import com.example.servicesapi.model.ServiceRequest;
import com.example.servicesapi.repository.PymeRepository;
import com.example.servicesapi.repository.ServiceRatingRepository;
import com.example.servicesapi.repository.ServiceRepository;
import com.example.servicesapi.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private ServiceRatingRepository serviceRatingRepository;

    // Method to create a new service request
    public ServiceRequest createServiceRequest(ServiceRequest serviceRequest) {
        // Check if the service exists
        Optional<ServiceModel> serviceOptional = serviceRepository.findById(serviceRequest.getServiceId());
        if (serviceOptional.isEmpty()) {
            throw new IllegalArgumentException("Service not found.");
        }

        ServiceModel service = serviceOptional.get();

        // Check if the client is trying to request their own service
        Optional<Pyme> pymeOptional = pymeRepository.findByUserId(serviceRequest.getClientId());
        if (pymeOptional.isPresent() && pymeOptional.get().getId().equals(service.getPyme().getId())) {
            throw new IllegalArgumentException("Cannot create a service request for your own service.");
        }

        // Set the service name in the request
        serviceRequest.setServiceName(service.getName());

        // Set the initial status to "Pending"
        serviceRequest.setStatus("Pending");

        return serviceRequestRepository.save(serviceRequest);
    }

    // Method to update a service request
    public ServiceRequest updateServiceRequest(ServiceRequest serviceRequest) {
        return serviceRequestRepository.save(serviceRequest);
    }

    // Method to delete a service request by its ID and client ID
    public void deleteServiceRequest(String id, Long clientId) {
        serviceRequestRepository.deleteByIdAndClientId(id, clientId);
    }

    // Method to retrieve service requests by client ID
    public List<ServiceRequest> getAllServiceRequestsByClientId(Long clientId) {
        return serviceRequestRepository.findByClientId(clientId);
    }

    // Method to get a service request by its ID and client ID
    public Optional<ServiceRequest> getServiceRequestByIdAndClientId(String id, Long clientId) {
        return serviceRequestRepository.findByIdAndClientId(id, clientId);
    }

    // Method to get a service request by its ID
    public Optional<ServiceRequest> getServiceRequestById(String id) {
        return serviceRequestRepository.findById(id);
    }

    // Method to group service requests by status for a specific client
    public Map<String, List<ServiceRequest>> getServiceRequestsGroupedByStatus(Long clientId) {
        List<ServiceRequest> requests = serviceRequestRepository.findByClientId(clientId);
        return requests.stream().collect(Collectors.groupingBy(ServiceRequest::getStatus));
    }

    // Method to mark a service request as "On Process"
    public ServiceRequest updateRequestToOnProcess(String requestId) {
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (!"Pending".equals(request.getStatus())) {
            throw new IllegalArgumentException("Request is not in Pending status");
        }

        request.setStatus("On Process");
        return serviceRequestRepository.save(request);
    }

    // Method to mark a service request as "Complete"
    public ServiceRequest updateRequestToComplete(String requestId) {
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (!"On Process".equals(request.getStatus())) {
            throw new IllegalArgumentException("Request is not in On Process status");
        }

        request.setStatus("Complete");
        return serviceRequestRepository.save(request);
    }

    // Method to finalize a service request with a rating and comment, transitioning to "Finished"
    public RequestFinished finalizeRequestWithRating(String requestId, Double rating, String comment) {
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (!"Complete".equals(request.getStatus())) {
            throw new IllegalArgumentException("Request is not in Complete status");
        }

        RequestFinished finishedRequest = new RequestFinished();
        finishedRequest.setId(request.getId());
        finishedRequest.setServiceId(request.getServiceId());
        finishedRequest.setServiceName(request.getServiceName());
        finishedRequest.setClientId(request.getClientId());
        finishedRequest.setStatus("Finished");
        finishedRequest.setDetails(request.getDetails());
        finishedRequest.setRequestDate(request.getRequestDate());
        finishedRequest.setRating(rating);
        finishedRequest.setComment(comment);
        finishedRequest.setRatingDate(new Date());

        return serviceRequestRepository.save(finishedRequest);
    }

    // Method to cancel a service request
    public ServiceRequest cancelServiceRequest(String requestId) {
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if ("Finished".equals(request.getStatus())) {
            throw new IllegalArgumentException("Cannot cancel a finished request");
        }

        request.setStatus("Canceled");
        return serviceRequestRepository.save(request);
    }

    public ServiceRequestDetailedDTO getServiceRequestDetailed(Long serviceId) {
        ServiceModel service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found."));

        List<ServiceRatingDTO> ratings = getServiceRatings(serviceId);
        double averageRating = calculateAverageRating(
                ratings.stream().map(ServiceRatingDTO::getRating).collect(Collectors.toList())
        );

        return new ServiceRequestDetailedDTO(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getPrice(),
                averageRating,
                ratings
        );
    }

    private List<ServiceRatingDTO> getServiceRatings(Long serviceId) {
        List<ServiceRating> ratings = serviceRatingRepository.findByServiceId(serviceId);
        return ratings.stream()
                .map(rating -> new ServiceRatingDTO(
                        rating.getId(),
                        rating.getClientId(),
                        rating.getServiceId(),
                        rating.getRating(),
                        rating.getComment(),
                        rating.getRatingDate()))
                .collect(Collectors.toList());
    }

    private double calculateAverageRating(List<Double> ratings) {
        return ratings.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    public Map<String, List<ServiceRequest>> getServiceRequestsGroupedByStatusForPyme(Long pymeUserId) {
        // Step 1: Load all services related to the pymeUserId
        List<ServiceModel> services = serviceRepository.findAllByPyme_UserId(pymeUserId);

        // Extract serviceIds from the services list
        List<Long> serviceIds = services.stream()
                                         .map(ServiceModel::getId)
                                         .collect(Collectors.toList());

        // Step 2: Load all service requests related to the services
        List<ServiceRequest> requests = serviceRequestRepository.findAllByServiceIdIn(serviceIds);

        // Step 3: Group the service requests by their status
        return requests.stream()
                       .collect(Collectors.groupingBy(ServiceRequest::getStatus));
    }

    public ServiceRequest cancelRequestByPyme(String requestId, Long pymeUserId) {
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        // Ensure the request is in the 'Pending' status and belongs to a service of the Pyme
        if (!"Pending".equals(request.getStatus())) {
            throw new IllegalArgumentException("Request is not in Pending status");
        }

        ServiceModel service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        if (!service.getPyme().getUser().getId().equals(pymeUserId)) {
            throw new IllegalArgumentException("Unauthorized access");
        }

        request.setStatus("Canceled");
        return serviceRequestRepository.save(request);
    }

    // Method to update a pending request to on-process by Pyme
    public ServiceRequest updateRequestToOnProcessByPyme(String requestId, Long pymeUserId) {
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        // Ensure the request is in the 'Pending' status and belongs to a service of the Pyme
        if (!"Pending".equals(request.getStatus())) {
            throw new IllegalArgumentException("Request is not in Pending status");
        }

        ServiceModel service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        if (!service.getPyme().getUser().getId().equals(pymeUserId)) {
            throw new IllegalArgumentException("Unauthorized access");
        }

        request.setStatus("On Process");
        return serviceRequestRepository.save(request);
    }

    // Method to update an on-process request to complete by Pyme
    public ServiceRequest updateRequestToCompleteByPyme(String requestId, Long pymeUserId) {
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        // Ensure the request is in the 'On Process' status and belongs to a service of the Pyme
        if (!"On Process".equals(request.getStatus())) {
            throw new IllegalArgumentException("Request is not in On Process status");
        }

        ServiceModel service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        if (!service.getPyme().getUser().getId().equals(pymeUserId)) {
            throw new IllegalArgumentException("Unauthorized access");
        }

        request.setStatus("Complete");
        return serviceRequestRepository.save(request);
    }

    public ServiceRequest finalizeRequestWithRating(String requestId, Long clientId, Double rating, String comment) {
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
    
        if (!"Complete".equals(request.getStatus())) {
            throw new IllegalArgumentException("Request is not in Complete status");
        }
    
        ServiceRating serviceRating = new ServiceRating();
        serviceRating.setServiceId(request.getServiceId());
        serviceRating.setClientId(clientId);
        serviceRating.setRating(rating);
        serviceRating.setComment(comment);
        serviceRating.setRatingDate(new Date());
    
        serviceRatingRepository.save(serviceRating);
    
        request.setStatus("Finished");
        request.setId(serviceRating.getId());
    
        return serviceRequestRepository.save(request);
    }    
}
