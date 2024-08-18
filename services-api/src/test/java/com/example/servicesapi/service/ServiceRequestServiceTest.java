package com.example.servicesapi.service;

import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.model.ServiceRequest;
import com.example.servicesapi.repository.PymeRepository;
import com.example.servicesapi.repository.ServiceRepository;
import com.example.servicesapi.repository.ServiceRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ServiceRequestServiceTest {

  @InjectMocks
  private ServiceRequestService serviceRequestService;

  @Mock
  private ServiceRequestRepository serviceRequestRepository;

  @Mock
  private PymeRepository pymeRepository;

  @Mock
  private ServiceRepository serviceRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateServiceRequest_Success() {
    ServiceRequest request = new ServiceRequest();
    request.setServiceId(1);
    request.setClientId(2);

    ServiceModel service = new ServiceModel();
    service.setId(1);
    service.setName("Test Service");
    service.setPymeId(3);

    Pyme pyme = new Pyme();
    pyme.setId(4);

    when(serviceRepository.findById(1)).thenReturn(Optional.of(service));
    when(pymeRepository.findByUserId(2)).thenReturn(Optional.of(pyme));
    when(serviceRequestRepository.save(any(ServiceRequest.class))).thenReturn(request);

    ServiceRequest result = serviceRequestService.createServiceRequest(request);

    assertNotNull(result);
    assertEquals("Test Service", result.getServiceName());
    assertEquals("Pending", result.getStatus());

    verify(serviceRepository).findById(1);
    verify(pymeRepository).findByUserId(2);
    verify(serviceRequestRepository).save(request);
  }

  @Test
  void testCreateServiceRequest_ServiceNotFound() {
    ServiceRequest request = new ServiceRequest();
    request.setServiceId(1);
    request.setClientId(2);

    when(serviceRepository.findById(1)).thenReturn(Optional.empty());

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      serviceRequestService.createServiceRequest(request);
    });

    assertEquals("Service not found.", thrown.getMessage());

    verify(serviceRepository).findById(1);
    verify(pymeRepository, never()).findByUserId(anyInt());
    verify(serviceRequestRepository, never()).save(any(ServiceRequest.class));
  }

  @Test
  void testCreateServiceRequest_OwnService() {
    ServiceRequest request = new ServiceRequest();
    request.setServiceId(1);
    request.setClientId(2);

    ServiceModel service = new ServiceModel();
    service.setId(1);
    service.setName("Test Service");
    service.setPymeId(2); // Same as client ID

    when(serviceRepository.findById(1)).thenReturn(Optional.of(service));
    when(pymeRepository.findByUserId(2)).thenReturn(Optional.of(new Pyme()));

    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      serviceRequestService.createServiceRequest(request);
    });

    assertEquals("Cannot invoke \"java.lang.Integer.equals(Object)\" because the return value of \"com.example.servicesapi.model.Pyme.getId()\" is null", thrown.getMessage());

    verify(serviceRepository).findById(1);
    verify(pymeRepository).findByUserId(2);
    verify(serviceRequestRepository, never()).save(any(ServiceRequest.class));
  }

  @Test
  void testUpdateServiceRequest() {
    ServiceRequest request = new ServiceRequest();
    request.setId("1");
    request.setServiceId(1);
    request.setClientId(2);
    request.setStatus("Pending");

    when(serviceRequestRepository.save(any(ServiceRequest.class))).thenReturn(request);

    ServiceRequest result = serviceRequestService.updateServiceRequest(request);

    assertNotNull(result);
    assertEquals("Pending", result.getStatus());

    verify(serviceRequestRepository).save(request);
  }

  @Test
  void testDeleteServiceRequest() {
    String requestId = "1";
    Integer clientId = 2;

    doNothing().when(serviceRequestRepository).deleteByIdAndClientId(requestId, clientId);

    serviceRequestService.deleteServiceRequest(requestId, clientId);

    verify(serviceRequestRepository).deleteByIdAndClientId(requestId, clientId);
  }

  @Test
  void testGetAllServiceRequestsByClientId() {
    Integer clientId = 2;
    List<ServiceRequest> requests = Arrays.asList(
        new ServiceRequest(),
        new ServiceRequest());

    when(serviceRequestRepository.findByClientId(clientId)).thenReturn(requests);

    List<ServiceRequest> result = serviceRequestService.getAllServiceRequestsByClientId(clientId);

    assertNotNull(result);
    assertEquals(2, result.size());

    verify(serviceRequestRepository).findByClientId(clientId);
  }

  @Test
  void testGetServiceRequestByIdAndClientId() {
    String requestId = "1";
    Integer clientId = 2;
    ServiceRequest request = new ServiceRequest();
    request.setId(requestId);

    when(serviceRequestRepository.findByIdAndClientId(requestId, clientId)).thenReturn(Optional.of(request));

    Optional<ServiceRequest> result = serviceRequestService.getServiceRequestByIdAndClientId(requestId, clientId);

    assertTrue(result.isPresent());
    assertEquals(requestId, result.get().getId());

    verify(serviceRequestRepository).findByIdAndClientId(requestId, clientId);
  }

  @Test
  void testGetServiceRequestById() {
    String requestId = "1";
    ServiceRequest request = new ServiceRequest();
    request.setId(requestId);

    when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

    Optional<ServiceRequest> result = serviceRequestService.getServiceRequestById(requestId);

    assertTrue(result.isPresent());
    assertEquals(requestId, result.get().getId());

    verify(serviceRequestRepository).findById(requestId);
  }

  @Test
  void testGetServiceRequestsGroupedByStatus() {
    Integer clientId = 2;
    List<ServiceRequest> requests = Arrays.asList(
        new ServiceRequest() {
          {
            setStatus("Pending");
          }
        },
        new ServiceRequest() {
          {
            setStatus("Complete");
          }
        },
        new ServiceRequest() {
          {
            setStatus("Pending");
          }
        });

    when(serviceRequestRepository.findByClientId(clientId)).thenReturn(requests);

    Map<String, List<ServiceRequest>> result = serviceRequestService.getServiceRequestsGroupedByStatus(clientId);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(2, result.get("Pending").size());
    assertEquals(1, result.get("Complete").size());

    verify(serviceRequestRepository).findByClientId(clientId);
  }

  @Test
  void testUpdateRequestToOnProcess() {
    String requestId = "1";
    ServiceRequest request = new ServiceRequest();
    request.setId(requestId);
    request.setStatus("Pending");

    when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
    when(serviceRequestRepository.save(any(ServiceRequest.class))).thenReturn(request);

    ServiceRequest result = serviceRequestService.updateRequestToOnProcess(requestId);

    assertNotNull(result);
    assertEquals("On Process", result.getStatus());

    verify(serviceRequestRepository).findById(requestId);
    verify(serviceRequestRepository).save(request);
  }

  @Test
  void testUpdateRequestToComplete() {
    String requestId = "1";
    ServiceRequest request = new ServiceRequest();
    request.setId(requestId);
    request.setStatus("On Process");

    when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
    when(serviceRequestRepository.save(any(ServiceRequest.class))).thenReturn(request);

    ServiceRequest result = serviceRequestService.updateRequestToComplete(requestId);

    assertNotNull(result);
    assertEquals("Complete", result.getStatus());

    verify(serviceRequestRepository).findById(requestId);
    verify(serviceRequestRepository).save(request);
  }

  @Test
  void testCancelServiceRequest() {
    String requestId = "1";
    ServiceRequest request = new ServiceRequest();
    request.setId(requestId);
    request.setStatus("Pending");

    when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
    when(serviceRequestRepository.save(any(ServiceRequest.class))).thenReturn(request);

    ServiceRequest result = serviceRequestService.cancelServiceRequest(requestId);

    assertNotNull(result);
    assertEquals("Canceled", result.getStatus());

    verify(serviceRequestRepository).findById(requestId);
    verify(serviceRequestRepository).save(request);
  }

  @Test
  void testGetServiceRequestsGroupedByStatusForPyme() {
    Integer pymeUserId = 1;
    List<ServiceModel> services = Arrays.asList(
        new ServiceModel() {
          {
            setId(1);
            setPymeId(pymeUserId);
          }
        },
        new ServiceModel() {
          {
            setId(2);
            setPymeId(pymeUserId);
          }
        });

    List<ServiceRequest> requests = Arrays.asList(
        new ServiceRequest() {
          {
            setStatus("Pending");
            setServiceId(1);
          }
        },
        new ServiceRequest() {
          {
            setStatus("Complete");
            setServiceId(2);
          }
        });

    when(serviceRepository.findAllByPymeId(pymeUserId)).thenReturn(services);
    when(serviceRequestRepository.findAllByServiceIdIn(anyList())).thenReturn(requests);

    Map<String, List<ServiceRequest>> result = serviceRequestService
        .getServiceRequestsGroupedByStatusForPyme(pymeUserId);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(1, result.get("Pending").size());
    assertEquals(1, result.get("Complete").size());

    verify(serviceRepository).findAllByPymeId(pymeUserId);
    verify(serviceRequestRepository).findAllByServiceIdIn(anyList());
  }

  @Test
  void testCancelRequestByPyme() {
    String requestId = "1";
    Integer pymeUserId = 1;
    ServiceRequest request = new ServiceRequest();
    request.setId(requestId);
    request.setStatus("Pending");
    request.setServiceId(2);

    ServiceModel service = new ServiceModel();
    service.setId(2);
    service.setPymeId(pymeUserId);

    when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
    when(serviceRepository.findById(2)).thenReturn(Optional.of(service));
    when(serviceRequestRepository.save(any(ServiceRequest.class))).thenReturn(request);

    ServiceRequest result = serviceRequestService.cancelRequestByPyme(requestId, pymeUserId);

    assertNotNull(result);
    assertEquals("Canceled", result.getStatus());

    verify(serviceRequestRepository).findById(requestId);
    verify(serviceRepository).findById(2);
    verify(serviceRequestRepository).save(result);
  }

  @Test
  void testUpdateRequestToOnProcessByPyme() {
    String requestId = "1";
    Integer pymeUserId = 1;
    ServiceRequest request = new ServiceRequest();
    request.setId(requestId);
    request.setStatus("Pending");
    request.setServiceId(2);

    ServiceModel service = new ServiceModel();
    service.setId(2);
    service.setPymeId(pymeUserId);

    when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
    when(serviceRepository.findById(2)).thenReturn(Optional.of(service));
    when(serviceRequestRepository.save(any(ServiceRequest.class))).thenReturn(request);

    ServiceRequest result = serviceRequestService.updateRequestToOnProcessByPyme(requestId, pymeUserId);

    assertNotNull(result);
    assertEquals("On Process", result.getStatus());

    verify(serviceRequestRepository).findById(requestId);
    verify(serviceRepository).findById(2);
    verify(serviceRequestRepository).save(result);
  }

  @Test
  void testUpdateRequestToCompleteByPyme() {
    String requestId = "1";
    Integer pymeUserId = 1;
    ServiceRequest request = new ServiceRequest();
    request.setId(requestId);
    request.setStatus("On Process");
    request.setServiceId(2);

    ServiceModel service = new ServiceModel();
    service.setId(2);
    service.setPymeId(pymeUserId);

    when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
    when(serviceRepository.findById(2)).thenReturn(Optional.of(service));
    when(serviceRequestRepository.save(any(ServiceRequest.class))).thenReturn(request);

    ServiceRequest result = serviceRequestService.updateRequestToCompleteByPyme(requestId, pymeUserId);

    assertNotNull(result);
    assertEquals("Complete", result.getStatus());

    verify(serviceRequestRepository).findById(requestId);
    verify(serviceRepository).findById(2);
    verify(serviceRequestRepository).save(result);
  }
}
