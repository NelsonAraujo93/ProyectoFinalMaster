package com.example.servicesapi.controller;

import com.example.servicesapi.dto.FinalizeRequestDTO;
import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.model.ServiceRequest;
import com.example.servicesapi.repository.PymeRepository;
import com.example.servicesapi.repository.ServiceRepository;
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
    private ServiceRepository serviceRepository;

    @Autowired
    private PymeRepository pymeRepository;

    // Create a new service request
    @PostMapping
    public ResponseEntity<ServiceRequest> createServiceRequest(
            @RequestBody ServiceRequest serviceRequest,
            @RequestHeader("x-auth-user-id") Integer clientId) {

        serviceRequest.setClientId(clientId);
        serviceRequest.setRequestDate(new Date());
        serviceRequest.setStatus("Pending");

        Optional<ServiceModel> serviceOptional = serviceRepository.findById(serviceRequest.getServiceId());

        if (serviceOptional.isPresent()) {
            ServiceModel serviceModel = serviceOptional.get();
            Optional<Pyme> pymeOptional = pymeRepository.findById(serviceModel.getPymeId());
    
            if (pymeOptional.isPresent()) {
                Pyme pyme = pymeOptional.get();
                if (pyme.getUser().getId().equals(clientId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
    
                ServiceRequest createdRequest = serviceRequestService.createServiceRequest(serviceRequest);
                return ResponseEntity.ok(createdRequest);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Get all service requests for the authenticated client
    @GetMapping
    public ResponseEntity<List<ServiceRequest>> getAllServiceRequests(
            @RequestHeader("x-auth-user-id") Integer clientId) {
        List<ServiceRequest> requests = serviceRequestService.getAllServiceRequestsByClientId(clientId);
        return ResponseEntity.ok(requests);
    }

    // Get a specific service request by ID for the authenticated client
    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequest> getServiceRequestById(
            @PathVariable String id,
            @RequestHeader("x-auth-user-id") Integer clientId) {
        Optional<ServiceRequest> serviceRequest = serviceRequestService.getServiceRequestByIdAndClientId(id, clientId);
        return serviceRequest.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a specific service request by ID for the authenticated client
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceRequest(
            @PathVariable String id,
            @RequestHeader("x-auth-user-id") Integer clientId) {
        serviceRequestService.deleteServiceRequest(id, clientId);
        return ResponseEntity.noContent().build();
    }

    // Update the status of a service request
    @PatchMapping("/{id}/status")
    public ResponseEntity<ServiceRequest> updateServiceRequestStatus(
            @PathVariable String id,
            @RequestBody String status,
            @RequestHeader("x-auth-user-id") Long pymeUserId) {

        Optional<ServiceRequest> serviceRequest = serviceRequestService.getServiceRequestById(id);

        if (serviceRequest.isPresent()) {
            ServiceModel serviceModel = serviceRepository.findById(serviceRequest.get().getServiceId()).orElse(null);

            if (serviceModel != null && serviceModel.getPymeId().equals(pymeUserId)) {
                if (("Pending".equals(serviceRequest.get().getStatus())
                        && ("On Process".equals(status) || "Rejected".equals(status))) ||
                        ("On Process".equals(serviceRequest.get().getStatus()) && "Completed".equals(status))) {
                    serviceRequest.get().setStatus(status);
                    ServiceRequest updatedRequest = serviceRequestService.updateServiceRequest(serviceRequest.get());
                    return ResponseEntity.ok(updatedRequest);
                }
                return ResponseEntity.badRequest().body(null);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/grouped")
    public ResponseEntity<Map<String, List<ServiceRequest>>> getGroupedServiceRequests(
            @RequestHeader("x-auth-user-id") Integer clientId) {
        Map<String, List<ServiceRequest>> groupedRequests = serviceRequestService
                .getServiceRequestsGroupedByStatus(clientId);
        return ResponseEntity.ok(groupedRequests);
    }

    @GetMapping("/pyme/requests")
    public ResponseEntity<Map<String, List<ServiceRequest>>> getPymeServiceRequests(
            @RequestHeader("x-auth-user-id") Integer pymeUserId) {

        Optional<Pyme> pyme = pymeRepository.findByUserId(pymeUserId);
        Map<String, List<ServiceRequest>> groupedRequests = serviceRequestService
                .getServiceRequestsGroupedByStatusForPyme(pyme.get().getId());
        return ResponseEntity.ok(groupedRequests);
    }

    // Pyme cancels a request
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ServiceRequest> cancelRequestByPyme(
            @PathVariable String id,
            @RequestHeader("x-auth-user-id") Integer pymeUserId) {

        Optional<Pyme> pyme = pymeRepository.findByUserId(pymeUserId);        
        ServiceRequest updatedRequest = serviceRequestService.cancelRequestByPyme(id, pyme.get().getId());
        return ResponseEntity.ok(updatedRequest);
    }

    // Pyme updates a request to on-process
    @PatchMapping("/{id}/on-process")
    public ResponseEntity<ServiceRequest> updateRequestToOnProcessByPyme(
            @PathVariable String id,
            @RequestHeader("x-auth-user-id") Integer pymeUserId) {
        Optional<Pyme> pyme = pymeRepository.findByUserId(pymeUserId);
        ServiceRequest updatedRequest = serviceRequestService.updateRequestToOnProcessByPyme(id, pyme.get().getId());
        return ResponseEntity.ok(updatedRequest);
    }

    // Pyme updates a request to complete
    @PatchMapping("/{id}/complete")
    public ResponseEntity<ServiceRequest> updateRequestToCompleteByPyme(
            @PathVariable String id,
            @RequestHeader("x-auth-user-id") Integer pymeUserId) {
        Optional<Pyme> pyme = pymeRepository.findByUserId(pymeUserId);
        ServiceRequest updatedRequest = serviceRequestService.updateRequestToCompleteByPyme(id, pyme.get().getId());
        return ResponseEntity.ok(updatedRequest);
    }

    // Client finalizes a request with a rating and comment
    @PatchMapping("/{id}/finalize")
    public ResponseEntity<ServiceRequest> finalizeRequestByClient(
            @PathVariable String id,
            @RequestHeader("x-auth-user-id") Integer clientId,
            @RequestBody FinalizeRequestDTO finalizeRequestDTO) {

        ServiceRequest updatedRequest = serviceRequestService.finalizeServiceRequest(
                id, finalizeRequestDTO.getRating(), finalizeRequestDTO.getComment());
        return ResponseEntity.ok(updatedRequest);
    }
}
