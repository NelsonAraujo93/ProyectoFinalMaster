package com.example.servicesapi.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceDTOTest {

  @Test
  public void testGettersAndSetters() {
    // Create a ServiceDTO instance with sample data
    ServiceDTO serviceDTO = new ServiceDTO();

    // Set properties
    serviceDTO.setId(1);
    serviceDTO.setName("Service 1");
    serviceDTO.setDescription("Description of Service 1");
    serviceDTO.setPrice(99.99);
    serviceDTO.setAverageRating(4.7);
    serviceDTO.setRatingCount(100);
    serviceDTO.setPymeId(10);
    serviceDTO.setPymeName("Pyme Name");
    serviceDTO.setPymeDescription("Description of Pyme");
    serviceDTO.setPymePhone("123-456-7890");
    serviceDTO.setPymePostalCode("12345");

    // Assert getters
    assertEquals(1, serviceDTO.getId());
    assertEquals("Service 1", serviceDTO.getName());
    assertEquals("Description of Service 1", serviceDTO.getDescription());
    assertEquals(99.99, serviceDTO.getPrice());
    assertEquals(4.7, serviceDTO.getAverageRating());
    assertEquals(100, serviceDTO.getRatingCount());
    assertEquals(10, serviceDTO.getPymeId());
    assertEquals("Pyme Name", serviceDTO.getPymeName());
    assertEquals("Description of Pyme", serviceDTO.getPymeDescription());
    assertEquals("123-456-7890", serviceDTO.getPymePhone());
    assertEquals("12345", serviceDTO.getPymePostalCode());
  }
}
