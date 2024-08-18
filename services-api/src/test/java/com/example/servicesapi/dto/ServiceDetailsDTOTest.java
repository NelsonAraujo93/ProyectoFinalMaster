package com.example.servicesapi.dto;

import com.example.servicesapi.model.ServiceRequest;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceDetailsDTOTest {

  @Test
  public void testGettersAndSetters() {
    // Create a ServiceDTO instance with sample data
    ServiceDTO serviceDTO = new ServiceDTO();
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

    // Create ServiceRequest instances with sample data
    ServiceRequest request1 = new ServiceRequest();
    request1.setId("1");
    request1.setServiceName("Service 1");
    request1.setServiceId(1);
    request1.setClientId(1);
    request1.setRequestDate(new java.util.Date());
    request1.setStatus("Pending");
    request1.setDetails("Details for Service 1");

    ServiceRequest request2 = new ServiceRequest();
    request2.setId("2");
    request2.setServiceName("Service 2");
    request2.setServiceId(2);
    request2.setClientId(2);
    request2.setRequestDate(new java.util.Date());
    request2.setStatus("Completed");
    request2.setDetails("Details for Service 2");

    // Create a ServiceDetailsDTO instance and set properties
    ServiceDetailsDTO serviceDetailsDTO = new ServiceDetailsDTO();
    serviceDetailsDTO.setService(serviceDTO);
    serviceDetailsDTO.setRequests(Arrays.asList(request1, request2));

    // Assert getters
    assertEquals(serviceDTO, serviceDetailsDTO.getService());
    assertEquals(Arrays.asList(request1, request2), serviceDetailsDTO.getRequests());
  }
}
