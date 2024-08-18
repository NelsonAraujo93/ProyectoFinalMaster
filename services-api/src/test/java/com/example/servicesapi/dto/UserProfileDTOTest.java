package com.example.servicesapi.dto;

import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.model.ServiceRequest;
import com.example.servicesapi.model.Pyme;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class UserProfileDTOTest {

  @Test
  public void testGettersAndSetters() {
    // Create test data
    ServiceModel serviceModel1 = new ServiceModel();
    serviceModel1.setId(1);
    serviceModel1.setName("Service 1");
    serviceModel1.setDescription("Description 1");
    serviceModel1.setPrice(100.0);

    ServiceRequest serviceRequest1 = new ServiceRequest();
    serviceRequest1.setId("1");
    serviceRequest1.setServiceName("Service Request 1");
    serviceRequest1.setServiceId(1);
    serviceRequest1.setClientId(1);
    serviceRequest1.setRequestDate(new java.util.Date());
    serviceRequest1.setStatus("Pending");
    serviceRequest1.setDetails("Details for Service Request 1");

    Pyme pyme1 = new Pyme();
    pyme1.setId(1);
    pyme1.setPymeName("Pyme 1");

    UserProfileDTO userProfileDTO = new UserProfileDTO();

    // Set properties
    userProfileDTO.setUserId(1L);
    userProfileDTO.setRole("ROLE_USER");
    userProfileDTO.setServices(Collections.singletonList(serviceModel1));
    userProfileDTO.setRequests(Collections.singletonList(serviceRequest1));
    userProfileDTO.setPyme(pyme1);

    // Assert getters
    assertEquals(1L, userProfileDTO.getUserId());
    assertEquals("ROLE_USER", userProfileDTO.getRole());
    assertEquals(Collections.singletonList(serviceModel1), userProfileDTO.getServices());
    assertEquals(Collections.singletonList(serviceRequest1), userProfileDTO.getRequests());
    assertEquals(pyme1, userProfileDTO.getPyme());
  }
}
