package com.example.servicesapi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceRequestDTOTest {

  @Test
  public void testGettersAndSetters() {
    // Create a ServiceRequestDTO instance with sample data
    ServiceRequestDTO serviceRequestDTO = new ServiceRequestDTO(1, "Service 1", "Description 1", 100.0, 4.5);

    // Set properties
    serviceRequestDTO.setId(1);
    serviceRequestDTO.setName("Service 1");
    serviceRequestDTO.setDescription("Description 1");
    serviceRequestDTO.setPrice(100.0);
    serviceRequestDTO.setAverageRating(4.5);

    // Assert getters
    assertEquals(1, serviceRequestDTO.getId());
    assertEquals("Service 1", serviceRequestDTO.getName());
    assertEquals("Description 1", serviceRequestDTO.getDescription());
    assertEquals(100.0, serviceRequestDTO.getPrice());
    assertEquals(4.5, serviceRequestDTO.getAverageRating());
  }
}
