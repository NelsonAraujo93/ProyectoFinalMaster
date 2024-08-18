package com.example.servicesapi.controller;

import com.example.servicesapi.dto.FinalizeRequestDTO;
import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.model.ServiceRequest;
import com.example.servicesapi.repository.PymeRepository;
import com.example.servicesapi.repository.ServiceRepository;
import com.example.servicesapi.service.ServiceRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ServiceRequestControllerTest {

    @InjectMocks
    private ServiceRequestController serviceRequestController;

    @Mock
    private ServiceRequestService serviceRequestService;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private PymeRepository pymeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateServiceRequest_FailedNotFound() {
        Integer clientId = 1;
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setServiceId(1);
        serviceRequest.setClientId(clientId);

        when(serviceRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<ServiceRequest> response = serviceRequestController.createServiceRequest(serviceRequest, clientId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetAllServiceRequests() {
        Integer clientId = 1;
        ServiceRequest request = new ServiceRequest();
        request.setClientId(clientId);

        when(serviceRequestService.getAllServiceRequestsByClientId(clientId)).thenReturn(Collections.singletonList(request));

        ResponseEntity<List<ServiceRequest>> response = serviceRequestController.getAllServiceRequests(clientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.singletonList(request), response.getBody());
    }

    @Test
    public void testGetServiceRequestById_Success() {
        String requestId = "1";
        Integer clientId = 1;
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setId(requestId);
        serviceRequest.setClientId(clientId);

        when(serviceRequestService.getServiceRequestByIdAndClientId(requestId, clientId)).thenReturn(Optional.of(serviceRequest));

        ResponseEntity<ServiceRequest> response = serviceRequestController.getServiceRequestById(requestId, clientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(serviceRequest, response.getBody());
    }

    @Test
    public void testGetServiceRequestById_NotFound() {
        String requestId = "1";
        Integer clientId = 1;

        when(serviceRequestService.getServiceRequestByIdAndClientId(requestId, clientId)).thenReturn(Optional.empty());

        ResponseEntity<ServiceRequest> response = serviceRequestController.getServiceRequestById(requestId, clientId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteServiceRequest() {
        String requestId = "1";
        Integer clientId = 1;

        doNothing().when(serviceRequestService).deleteServiceRequest(requestId, clientId);

        ResponseEntity<Void> response = serviceRequestController.deleteServiceRequest(requestId, clientId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testGetGroupedServiceRequests() {
        Integer clientId = 1;
        Map<String, List<ServiceRequest>> groupedRequests = Map.of("Pending", Collections.singletonList(new ServiceRequest()));

        when(serviceRequestService.getServiceRequestsGroupedByStatus(clientId)).thenReturn(groupedRequests);

        ResponseEntity<Map<String, List<ServiceRequest>>> response = serviceRequestController.getGroupedServiceRequests(clientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(groupedRequests, response.getBody());
    }

    @Test
    public void testGetPymeServiceRequests() {
        Integer pymeUserId = 1;
        Pyme pyme = new Pyme();
        pyme.setId(1);

        Map<String, List<ServiceRequest>> groupedRequests = Map.of("Pending", Collections.singletonList(new ServiceRequest()));

        when(pymeRepository.findByUserId(pymeUserId)).thenReturn(Optional.of(pyme));
        when(serviceRequestService.getServiceRequestsGroupedByStatusForPyme(pyme.getId())).thenReturn(groupedRequests);

        ResponseEntity<Map<String, List<ServiceRequest>>> response = serviceRequestController.getPymeServiceRequests(pymeUserId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(groupedRequests, response.getBody());
    }

    @Test
    public void testCancelRequestByPyme() {
        String requestId = "1";
        Integer pymeUserId = 1;
        Pyme pyme = new Pyme();
        pyme.setId(1);

        ServiceRequest updatedRequest = new ServiceRequest();
        updatedRequest.setId(requestId);

        when(pymeRepository.findByUserId(pymeUserId)).thenReturn(Optional.of(pyme));
        when(serviceRequestService.cancelRequestByPyme(requestId, pyme.getId())).thenReturn(updatedRequest);

        ResponseEntity<ServiceRequest> response = serviceRequestController.cancelRequestByPyme(requestId, pymeUserId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedRequest, response.getBody());
    }

    @Test
    public void testUpdateRequestToOnProcessByPyme() {
        String requestId = "1";
        Integer pymeUserId = 1;
        Pyme pyme = new Pyme();
        pyme.setId(1);

        ServiceRequest updatedRequest = new ServiceRequest();
        updatedRequest.setId(requestId);

        when(pymeRepository.findByUserId(pymeUserId)).thenReturn(Optional.of(pyme));
        when(serviceRequestService.updateRequestToOnProcessByPyme(requestId, pyme.getId())).thenReturn(updatedRequest);

        ResponseEntity<ServiceRequest> response = serviceRequestController.updateRequestToOnProcessByPyme(requestId, pymeUserId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedRequest, response.getBody());
    }

    @Test
    public void testUpdateRequestToCompleteByPyme() {
        String requestId = "1";
        Integer pymeUserId = 1;
        Pyme pyme = new Pyme();
        pyme.setId(1);

        ServiceRequest updatedRequest = new ServiceRequest();
        updatedRequest.setId(requestId);

        when(pymeRepository.findByUserId(pymeUserId)).thenReturn(Optional.of(pyme));
        when(serviceRequestService.updateRequestToCompleteByPyme(requestId, pyme.getId())).thenReturn(updatedRequest);

        ResponseEntity<ServiceRequest> response = serviceRequestController.updateRequestToCompleteByPyme(requestId, pymeUserId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedRequest, response.getBody());
    }

    @Test
    public void testFinalizeRequestByClient() {
        String requestId = "1";
        Integer clientId = 1;
        FinalizeRequestDTO finalizeRequestDTO = new FinalizeRequestDTO();
        finalizeRequestDTO.setRating(5);
        finalizeRequestDTO.setComment("Great service");

        ServiceRequest updatedRequest = new ServiceRequest();
        updatedRequest.setId(requestId);

        when(serviceRequestService.finalizeServiceRequest(requestId, 5, "Great service")).thenReturn(updatedRequest);

        ResponseEntity<ServiceRequest> response = serviceRequestController.finalizeRequestByClient(requestId, clientId, finalizeRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedRequest, response.getBody());
    }
}
