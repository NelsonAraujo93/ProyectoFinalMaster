package com.example.servicesapi.controller;

import com.example.servicesapi.model.ServiceRequest;
import com.example.servicesapi.service.ServiceRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/requests")
public class ServiceRequestController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRequestController.class);

    @Autowired
    private ServiceRequestService requestService;

    @PostMapping
    public ResponseEntity<ServiceRequest> createRequest(
            @RequestBody ServiceRequest request,
            @RequestHeader("x-auth-user-id") Long clientId) {
        logger.info("Creating new service request for clientId: {}", clientId);
        request.setClientId(clientId);
        request.setStatus("PENDING");  // Set status to PENDING by default
        request.setRequestDate(new Date());  // Set the current date as the requestDate
        ServiceRequest createdRequest = requestService.createServiceRequest(request);
        logger.info("Service request created with id: {}", createdRequest.getId());
        return ResponseEntity.ok(createdRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequest> getRequestById(
            @PathVariable Long id,
            @RequestHeader("x-auth-user-id") Long clientId) {
        logger.info("Fetching service request with id: {} for clientId: {}", id, clientId);
        Optional<ServiceRequest> request = requestService.getServiceRequestsByServiceIdAndClientId(id, clientId);
        return request.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Service request with id: {} not found for clientId: {}", id, clientId);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping
    public ResponseEntity<List<ServiceRequest>> getAllRequests(
            @RequestHeader("x-auth-user-id") Long clientId) {
        logger.info("Fetching all service requests for clientId: {}", clientId);
        List<ServiceRequest> requests = requestService.getAllServiceRequestsByClientId(clientId);
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceRequest> updateRequestStatus(
            @PathVariable String id,
            @RequestBody String status,
            @RequestHeader("x-auth-user-id") Long clientId) {
        logger.info("Updating status of service request with id: {} for clientId: {}", id, clientId);
        Optional<ServiceRequest> existingRequest = requestService.getServiceRequestByIdAndClientId(id, clientId);
        if (existingRequest.isPresent()) {
            ServiceRequest updatedRequest = existingRequest.get();
            updatedRequest.setStatus(status);
            requestService.updateServiceRequest(updatedRequest);
            logger.info("Service request with id: {} updated to status: {}", id, status);
            return ResponseEntity.ok(updatedRequest);
        } else {
            logger.warn("Service request with id: {} not found for clientId: {}", id, clientId);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(
            @PathVariable String id,
            @RequestHeader("x-auth-user-id") Long clientId) {
        logger.info("Deleting service request with id: {} for clientId: {}", id, clientId);
        requestService.deleteServiceRequest(id, clientId);
        logger.info("Service request with id: {} deleted", id);
        return ResponseEntity.noContent().build();
    }
}
