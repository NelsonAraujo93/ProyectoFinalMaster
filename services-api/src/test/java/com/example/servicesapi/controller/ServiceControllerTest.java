package com.example.servicesapi.controller;

import com.example.servicesapi.dto.ServiceDTO;
import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.model.User;
import com.example.servicesapi.repository.PymeRepository;
import com.example.servicesapi.service.PymeService;
import com.example.servicesapi.service.ServiceService;
import com.example.servicesapi.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ServiceControllerTest {

  @InjectMocks
  private ServiceController serviceController;

  @Mock
  private ServiceService serviceService;

  @Mock
  private PymeRepository pymeRepository;

  @Mock
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateServicePymeNotFound() {
    ServiceModel serviceModel = new ServiceModel();
    when(pymeRepository.findByUserId(anyInt())).thenReturn(Optional.empty());

    ResponseEntity<ServiceDTO> response = serviceController.createService(serviceModel, 1);

    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
  }

  @Test
  void testGetServiceByIdNotFound() {
    when(serviceService.getServiceByIdAndUserId(anyInt(), anyInt())).thenReturn(Optional.empty());

    ResponseEntity<ServiceDTO> response = serviceController.getServiceById(1, 1);

    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
  }

  @Test
  void testGetAllServicesNoPyme() {
    when(pymeRepository.findByUserId(anyInt())).thenReturn(Optional.empty());

    ResponseEntity<List<ServiceDTO>> response = serviceController.getAllServices(1);

    assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    List<ServiceDTO> serviceDTOs = response.getBody();
    assertNotNull(serviceDTOs);
    assertTrue(serviceDTOs.isEmpty());
  }

  @Test
  void testDeleteService() {
    // Arrange
    doNothing().when(serviceService).deleteService(anyInt(), anyInt());

    // Act
    ResponseEntity<Void> response = serviceController.deleteService(1, 1);

    // Assert
    assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue());
    verify(serviceService).deleteService(1, 1); // Verify deleteService was called with the correct parameters
  }

  @Test
  void testConvertToDTO() {
    // Arrange
    ServiceModel serviceModel = new ServiceModel();
    serviceModel.setId(1);
    serviceModel.setName("Service Name");
    serviceModel.setDescription("Service Description");
    serviceModel.setPrice(100.0);
    serviceModel.setAverageRating(4.5);
    serviceModel.setRatingCount(10);
    serviceModel.setPymeId(1);

    Pyme pyme = new Pyme();
    pyme.setId(1);
    pyme.setPymeName("Pyme Name");
    pyme.setPymeDescription("Pyme Description");
    pyme.setPymePhone("123456789");
    pyme.setPymePostalCode("Postal Code");

    when(pymeRepository.findById(anyInt())).thenReturn(Optional.of(pyme));

    // Act
    ServiceDTO serviceDTO = serviceController.convertToDTO(serviceModel);

    // Assert
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
  void testUpdateService() {
    // Arrange
    ServiceModel serviceModel = new ServiceModel();
    serviceModel.setId(1);
    serviceModel.setName("Updated Service");

    Pyme pyme = new Pyme();
    pyme.setId(1);
    pyme.setPymeName("Pyme Name");

    ServiceModel updatedService = new ServiceModel();
    updatedService.setId(1);
    updatedService.setName("Updated Service");
    updatedService.setPymeId(1);

    ServiceDTO serviceDTO = new ServiceDTO();
    serviceDTO.setId(1);
    serviceDTO.setName("Updated Service");
    serviceDTO.setPymeName("Pyme Name");

    when(serviceService.updateService(any(ServiceModel.class), anyInt())).thenReturn(updatedService);
    when(pymeRepository.findById(anyInt())).thenAnswer((Answer<Optional<Pyme>>) invocation -> {
      Integer id = invocation.getArgument(0);
      return id.equals(1) ? Optional.of(pyme) : Optional.empty();
    });

    // Act
    ResponseEntity<ServiceDTO> response = serviceController.updateService(1, serviceModel, 1);

    // Assert
    assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
  }

  @Test
  void testGetAllServicesWithoutPyme() {
    // Arrange
    when(pymeRepository.findByUserId(anyInt())).thenReturn(Optional.empty());

    // Act
    ResponseEntity<List<ServiceDTO>> response = serviceController.getAllServices(1);

    // Assert
    assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    assertEquals(Collections.emptyList(), response.getBody());
  }
}
