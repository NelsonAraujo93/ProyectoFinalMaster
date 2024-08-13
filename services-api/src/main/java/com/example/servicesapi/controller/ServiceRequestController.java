package com.example.servicesapi.controller;

import com.example.servicesapi.dto.FinalizeRequestDTO;
import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.model.ServiceRating;
import com.example.servicesapi.model.ServiceRequest;
import com.example.servicesapi.repository.ServiceRepository;
import com.example.servicesapi.service.ServiceRatingService;
import com.example.servicesapi.service.ServiceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/requests")
public class ServiceRequestController {

    @Autowired
    private ServiceRequestService serviceRequestService;

    @Autowired
    private ServiceRatingService serviceRatingService;

    @Autowired
    private ServiceRepository serviceRepository;

    // Create a new service request
    @PostMapping
    public ResponseEntity<ServiceRequest> createServiceRequest(
            @RequestBody ServiceRequest serviceRequest,
            @RequestHeader("x-auth-user-id") Long clientId) {

        serviceRequest.setClientId(clientId);
        serviceRequest.setRequestDate(new Date());
        serviceRequest.setStatus("PENDING"); // Default status

        // Fetch the ServiceModel based on serviceId from ServiceRequest
        Optional<ServiceModel> serviceOptional = serviceRepository.findById(serviceRequest.getServiceId());

        if (serviceOptional.isPresent()) {
            ServiceModel serviceModel = serviceOptional.get();

            // Ensure that the client is not trying to request their own service
            if (serviceModel.getPyme().getUser().getId().equals(clientId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            // Proceed with creating the service request
            ServiceRequest createdRequest = serviceRequestService.createServiceRequest(serviceRequest);
            return ResponseEntity.ok(createdRequest);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Get all service requests for the authenticated client
    @GetMapping
    public ResponseEntity<List<ServiceRequest>> getAllServiceRequests(
            @RequestHeader("x-auth-user-id") Long clientId) {
        List<ServiceRequest> requests = serviceRequestService.getAllServiceRequestsByClientId(clientId);
        return ResponseEntity.ok(requests);
    }

    // Get a specific service request by ID for the authenticated client
    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequest> getServiceRequestById(
            @PathVariable String id,
            @RequestHeader("x-auth-user-id") Long clientId) {
        Optional<ServiceRequest> serviceRequest = serviceRequestService.getServiceRequestByIdAndClientId(id, clientId);
        return serviceRequest.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a specific service request by ID for the authenticated client
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceRequest(
            @PathVariable String id,
            @RequestHeader("x-auth-user-id") Long clientId) {
        serviceRequestService.deleteServiceRequest(id, clientId);
        return ResponseEntity.noContent().build();
    }

    // Rate a completed service request
    @PostMapping("/{id}/rate")
    public ResponseEntity<ServiceRating> rateService(
            @PathVariable String id,
            @RequestBody ServiceRating serviceRating,
            @RequestHeader("x-auth-user-id") Long clientId) {

        // Check if the service request exists and belongs to the client
        Optional<ServiceRequest> serviceRequest = serviceRequestService.getServiceRequestByIdAndClientId(id, clientId);

        if (serviceRequest.isPresent()) {
            // Ensure the service request is completed before allowing a rating
            if ("COMPLETED".equals(serviceRequest.get().getStatus())) {
                serviceRating.setServiceId(serviceRequest.get().getServiceId());
                serviceRating.setClientId(clientId);
                serviceRating.setRatingDate(new Date());
                ServiceRating createdRating = serviceRatingService.createServiceRating(serviceRating);

                // Update the service request status to FINISHED after rating
                ServiceRequest updatedRequest = serviceRequestService.finalizeRequestWithRating(id,
                        serviceRating.getRating(), serviceRating.getComment());

                return ResponseEntity.ok(createdRating);
            } else {
                return ResponseEntity.badRequest().body(null); // Request not completed
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update the status of a service request to ON_PROCESS, COMPLETED, or REJECTED
    @PatchMapping("/{id}/status")
    public ResponseEntity<ServiceRequest> updateServiceRequestStatus(
            @PathVariable String id,
            @RequestBody String status,
            @RequestHeader("x-auth-user-id") Long pymeUserId) {

        // Fetch the service request and ensure it belongs to the authenticated pyme
        Optional<ServiceRequest> serviceRequest = serviceRequestService.getServiceRequestById(id);

        if (serviceRequest.isPresent()) {
            ServiceModel serviceModel = serviceRepository.findById(serviceRequest.get().getServiceId()).orElse(null);

            if (serviceModel != null && serviceModel.getPyme().getUser().getId().equals(pymeUserId)) {
                // Only allow transition to ON_PROCESS, COMPLETED, or REJECTED from appropriate
                // states
                if (("PENDING".equals(serviceRequest.get().getStatus())
                        && ("ON_PROCESS".equals(status) || "REJECTED".equals(status))) ||
                        ("ON_PROCESS".equals(serviceRequest.get().getStatus()) && "COMPLETED".equals(status))) {
                    serviceRequest.get().setStatus(status);
                    ServiceRequest updatedRequest = serviceRequestService.updateServiceRequest(serviceRequest.get());
                    return ResponseEntity.ok(updatedRequest);
                }
                return ResponseEntity.badRequest().body(null); // Invalid status transition
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Unauthorized access
            }
        } else {
            return ResponseEntity.notFound().build(); // Request not found
        }
    }

    @GetMapping("/grouped")
    public ResponseEntity<Map<String, List<ServiceRequest>>> getGroupedServiceRequests(
            @RequestHeader("x-auth-user-id") Long clientId) {
        Map<String, List<ServiceRequest>> groupedRequests = serviceRequestService
                .getServiceRequestsGroupedByStatus(clientId);
        return ResponseEntity.ok(groupedRequests);
    }

    @GetMapping("/pyme/requests")
    public ResponseEntity<Map<String, List<ServiceRequest>>> getPymeServiceRequests(
            @RequestHeader("x-auth-user-id") Long pymeUserId) {

        Map<String, List<ServiceRequest>> groupedRequests = serviceRequestService
                .getServiceRequestsGroupedByStatusForPyme(pymeUserId);
        return ResponseEntity.ok(groupedRequests);
    }

    // Pyme cancels a request
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ServiceRequest> cancelRequestByPyme(
            @PathVariable String id,
            @RequestHeader("x-auth-user-id") Long pymeUserId) {
        ServiceRequest updatedRequest = serviceRequestService.cancelRequestByPyme(id, pymeUserId);
        return ResponseEntity.ok(updatedRequest);
    }

    // Pyme updates a request to on-process
    @PatchMapping("/{id}/on-process")
    public ResponseEntity<ServiceRequest> updateRequestToOnProcessByPyme(
            @PathVariable String id,
            @RequestHeader("x-auth-user-id") Long pymeUserId) {
        ServiceRequest updatedRequest = serviceRequestService.updateRequestToOnProcessByPyme(id, pymeUserId);
        return ResponseEntity.ok(updatedRequest);
    }

    // Pyme updates a request to complete
    @PatchMapping("/{id}/complete")
    public ResponseEntity<ServiceRequest> updateRequestToCompleteByPyme(
            @PathVariable String id,
            @RequestHeader("x-auth-user-id") Long pymeUserId) {
        ServiceRequest updatedRequest = serviceRequestService.updateRequestToCompleteByPyme(id, pymeUserId);
        return ResponseEntity.ok(updatedRequest);
    }

    @PatchMapping("/{id}/finalize")
    public ResponseEntity<ServiceRequest> finalizeRequestByClient(
            @PathVariable String id,
            @RequestHeader("x-auth-user-id") Long clientId,
            @RequestBody FinalizeRequestDTO finalizeRequestDTO) {
    
        ServiceRequest updatedRequest = serviceRequestService.finalizeRequestWithRating(id, clientId, finalizeRequestDTO.getRating(), finalizeRequestDTO.getComment());
        return ResponseEntity.ok(updatedRequest);
    }
}
