package com.example.servicesapi.controller;

import com.example.servicesapi.dto.PymeDTO;
import com.example.servicesapi.dto.ServiceDTO;
import com.example.servicesapi.dto.ServiceDetailsDTO;
import com.example.servicesapi.dto.ServiceRequestDTO;
import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.model.ServiceRequest;
import com.example.servicesapi.repository.PymeRepository;
import com.example.servicesapi.repository.ServiceRepository;
import com.example.servicesapi.repository.ServiceRequestRepository;
import com.example.servicesapi.service.PymeService;
import com.example.servicesapi.service.ServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PublicServicesControllerTest {

  @InjectMocks
  private PublicServicesController publicServicesController;

  @Mock
  private PymeService pymeService;

  @Mock
  private ServiceService serviceService;

  @Mock
  private PymeRepository pymeRepository;

  @Mock
  private ServiceRepository serviceRepository;

  @Mock
  private ServiceRequestRepository serviceRequestRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAllPymes() {
    Pyme pyme = new Pyme();
    pyme.setPymeName("Test Pyme");
    List<Pyme> pymes = Collections.singletonList(pyme);

    when(pymeRepository.findAll()).thenReturn(pymes);

    ResponseEntity<List<Pyme>> response = publicServicesController.getAllPymes();

    assertEquals(200, response.getStatusCodeValue());
    List<Pyme> result = response.getBody();
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Test Pyme", result.get(0).getPymeName());
  }

  @Test
  void testGetLastCreatedPymes() {
    Pyme pyme = new Pyme();
    List<Pyme> pymes = Collections.singletonList(pyme);

    when(pymeRepository.findTop6ByOrderByIdDesc()).thenReturn(pymes);

    ResponseEntity<List<Pyme>> response = publicServicesController.getLastCreatedPymes();

    assertEquals(200, response.getStatusCodeValue());
    List<Pyme> result = response.getBody();
    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void testGetPymeByIdSuccess() {
    PymeDTO pymeDTO = new PymeDTO();
    when(pymeService.getPymeDTOById(anyInt())).thenReturn(Optional.of(pymeDTO));

    ResponseEntity<PymeDTO> response = publicServicesController.getPymeById(1);

    assertEquals(200, response.getStatusCodeValue());
    PymeDTO result = response.getBody();
    assertNotNull(result);
  }

  @Test
  void testGetPymeByIdNotFound() {
    when(pymeService.getPymeDTOById(anyInt())).thenReturn(Optional.empty());

    ResponseEntity<PymeDTO> response = publicServicesController.getPymeById(1);

    assertEquals(404, response.getStatusCodeValue());
  }

  @Test
  void testGetServiceByIdNotFound() {
    when(serviceService.getServiceById(anyInt())).thenReturn(Optional.empty());

    ResponseEntity<ServiceDetailsDTO> response = publicServicesController.getServiceById(1);

    assertEquals(404, response.getStatusCodeValue());
  }

  @Test
  void testFilterServices() {
    ServiceRequestDTO serviceRequestDTO = new ServiceRequestDTO();
    List<ServiceRequestDTO> serviceRequestDTOs = Collections.singletonList(serviceRequestDTO);

    when(serviceService.filterServices(any(), any(), any(), any())).thenReturn(serviceRequestDTOs);

    ResponseEntity<List<ServiceRequestDTO>> response = publicServicesController.filterServices(
        null, null, null, null);

    assertEquals(200, response.getStatusCodeValue());
    List<ServiceRequestDTO> result = response.getBody();
    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void testConvertToDTO() throws Exception {
    // Setup
    ServiceModel serviceModel = new ServiceModel();
    serviceModel.setId(1);
    serviceModel.setName("Test Service");
    serviceModel.setDescription("Test Description");
    serviceModel.setPrice(100.0);
    serviceModel.setAverageRating(4.5);
    serviceModel.setRatingCount(10);
    serviceModel.setPymeId(1);

    Pyme pyme = new Pyme();
    pyme.setId(1);
    pyme.setPymeName("Test Pyme");
    pyme.setPymeDescription("Test Pyme Description");
    pyme.setPymePhone("123456789");
    pyme.setPymePostalCode("54321");

    when(pymeRepository.findById(anyInt())).thenReturn(Optional.of(pyme));

    // Use reflection to access the private method
    Method method = PublicServicesController.class.getDeclaredMethod("convertToDTO", ServiceModel.class);
    method.setAccessible(true);

    // Invoke the method
    ServiceDTO serviceDTO = (ServiceDTO) method.invoke(publicServicesController, serviceModel);

    // Assertions
    assertNotNull(serviceDTO);
    assertEquals(serviceModel.getId(), serviceDTO.getId());
    assertEquals(serviceModel.getName(), serviceDTO.getName());
    assertEquals(serviceModel.getDescription(), serviceDTO.getDescription());
    assertEquals(serviceModel.getPrice(), serviceDTO.getPrice());
    assertEquals(serviceModel.getAverageRating(), serviceDTO.getAverageRating());
    assertEquals(serviceModel.getRatingCount(), serviceDTO.getRatingCount());
    assertEquals(pyme.getId(), serviceDTO.getPymeId());
    assertEquals(pyme.getPymeName(), serviceDTO.getPymeName());
    assertEquals(pyme.getPymeDescription(), serviceDTO.getPymeDescription());
    assertEquals(pyme.getPymePhone(), serviceDTO.getPymePhone());
    assertEquals(pyme.getPymePostalCode(), serviceDTO.getPymePostalCode());
  }

  @Test
  void testGetAllServices() {
    // Setup mock data
    ServiceModel serviceModel = new ServiceModel();
    serviceModel.setId(1);
    serviceModel.setName("Test Service");
    serviceModel.setDescription("Test Description");
    serviceModel.setPrice(100.0);
    serviceModel.setAverageRating(4.5);
    serviceModel.setRatingCount(10);
    serviceModel.setPymeId(1);

    Pyme pyme = new Pyme();
    pyme.setId(1);
    pyme.setPymeName("Test Pyme");
    pyme.setPymeDescription("Test Pyme Description");
    pyme.setPymePhone("123456789");
    pyme.setPymePostalCode("54321");

    when(serviceRepository.findAll()).thenReturn(Collections.singletonList(serviceModel));
    when(pymeRepository.findById(anyInt())).thenReturn(Optional.of(pyme));

    // Call the controller method
    ResponseEntity<List<ServiceDTO>> response = publicServicesController.getAllServices();

    // Validate response
    assertEquals(200, response.getStatusCodeValue());
    List<ServiceDTO> serviceDTOs = response.getBody();
    assertNotNull(serviceDTOs);
    assertEquals(1, serviceDTOs.size());

    ServiceDTO dto = serviceDTOs.get(0);
    assertEquals(serviceModel.getId(), dto.getId());
    assertEquals(serviceModel.getName(), dto.getName());
    assertEquals(serviceModel.getDescription(), dto.getDescription());
    assertEquals(serviceModel.getPrice(), dto.getPrice());
    assertEquals(serviceModel.getAverageRating(), dto.getAverageRating());
    assertEquals(serviceModel.getRatingCount(), dto.getRatingCount());
    assertEquals(pyme.getId(), dto.getPymeId());
    assertEquals(pyme.getPymeName(), dto.getPymeName());
    assertEquals(pyme.getPymeDescription(), dto.getPymeDescription());
    assertEquals(pyme.getPymePhone(), dto.getPymePhone());
    assertEquals(pyme.getPymePostalCode(), dto.getPymePostalCode());
  }

  @Test
  void testGetLastCreatedServices() {
    // Setup mock data
    ServiceModel serviceModel = new ServiceModel();
    serviceModel.setId(1);
    serviceModel.setName("Latest Service");
    serviceModel.setDescription("Latest Description");
    serviceModel.setPrice(200.0);
    serviceModel.setAverageRating(5.0);
    serviceModel.setRatingCount(20);
    serviceModel.setPymeId(2);

    Pyme pyme = new Pyme();
    pyme.setId(2);
    pyme.setPymeName("Latest Pyme");
    pyme.setPymeDescription("Latest Pyme Description");
    pyme.setPymePhone("987654321");
    pyme.setPymePostalCode("54321");

    when(serviceRepository.findTop6ByOrderByIdDesc()).thenReturn(Collections.singletonList(serviceModel));
    when(pymeRepository.findById(anyInt())).thenReturn(Optional.of(pyme));

    // Call the controller method
    ResponseEntity<List<ServiceDTO>> response = publicServicesController.getLastCreatedServices();

    // Validate response
    assertEquals(200, response.getStatusCodeValue());
    List<ServiceDTO> serviceDTOs = response.getBody();
    assertNotNull(serviceDTOs);
    assertEquals(1, serviceDTOs.size());

    ServiceDTO dto = serviceDTOs.get(0);
    assertEquals(serviceModel.getId(), dto.getId());
    assertEquals(serviceModel.getName(), dto.getName());
    assertEquals(serviceModel.getDescription(), dto.getDescription());
    assertEquals(serviceModel.getPrice(), dto.getPrice());
    assertEquals(serviceModel.getAverageRating(), dto.getAverageRating());
    assertEquals(serviceModel.getRatingCount(), dto.getRatingCount());
    assertEquals(pyme.getId(), dto.getPymeId());
    assertEquals(pyme.getPymeName(), dto.getPymeName());
    assertEquals(pyme.getPymeDescription(), dto.getPymeDescription());
    assertEquals(pyme.getPymePhone(), dto.getPymePhone());
    assertEquals(pyme.getPymePostalCode(), dto.getPymePostalCode());
  }

  @Test
  void testGetServiceById() {
    // Setup mock data
    ServiceModel serviceModel = new ServiceModel();
    serviceModel.setId(1);
    serviceModel.setName("Service By Id");
    serviceModel.setDescription("Description By Id");
    serviceModel.setPrice(300.0);
    serviceModel.setAverageRating(4.0);
    serviceModel.setRatingCount(30);
    serviceModel.setPymeId(3);

    Pyme pyme = new Pyme();
    pyme.setId(3);
    pyme.setPymeName("Pyme By Id");
    pyme.setPymeDescription("Pyme Description By Id");
    pyme.setPymePhone("123123123");
    pyme.setPymePostalCode("54321");

    ServiceRequest serviceRequest = new ServiceRequest();
    serviceRequest.setId("1");
    serviceRequest.setStatus("Finalized");
    serviceRequest.setRatingDate(new Date());

    when(serviceService.getServiceById(anyInt())).thenReturn(Optional.of(serviceModel));
    when(serviceRequestRepository.findTop3ByServiceIdAndStatusOrderByRatingDateDesc(anyInt(), anyString()))
        .thenReturn(Collections.singletonList(serviceRequest));
    when(pymeRepository.findById(anyInt())).thenReturn(Optional.of(pyme));

    // Call the controller method
    ResponseEntity<ServiceDetailsDTO> response = publicServicesController.getServiceById(1);

    // Validate response
    assertEquals(200, response.getStatusCodeValue());
    ServiceDetailsDTO serviceDetailsDTO = response.getBody();
    assertNotNull(serviceDetailsDTO);
    assertEquals(serviceModel.getId(), serviceDetailsDTO.getService().getId());
    assertEquals(serviceModel.getName(), serviceDetailsDTO.getService().getName());
    assertEquals(serviceModel.getDescription(), serviceDetailsDTO.getService().getDescription());
    assertEquals(serviceModel.getPrice(), serviceDetailsDTO.getService().getPrice());
    assertEquals(serviceModel.getAverageRating(), serviceDetailsDTO.getService().getAverageRating());
    assertEquals(serviceModel.getRatingCount(), serviceDetailsDTO.getService().getRatingCount());

    // Validate the list of requests
    assertNotNull(serviceDetailsDTO.getRequests());
    assertEquals(1, serviceDetailsDTO.getRequests().size());
    assertEquals(serviceRequest.getId(), serviceDetailsDTO.getRequests().get(0).getId());
    assertEquals(serviceRequest.getStatus(), serviceDetailsDTO.getRequests().get(0).getStatus());
  }

}
