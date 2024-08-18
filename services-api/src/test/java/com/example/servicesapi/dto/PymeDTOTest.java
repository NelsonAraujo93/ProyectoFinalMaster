package com.example.servicesapi.dto;

import com.example.servicesapi.model.ServiceModel;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PymeDTOTest {

  @Test
  public void testGettersAndSetters() {
    // Create ServiceModel instances with sample data
    ServiceModel serviceModel1 = new ServiceModel();
    serviceModel1.setId(1);
    serviceModel1.setName("Service 1");
    serviceModel1.setDescription("Description of Service 1");
    serviceModel1.setPrice(100.0);
    serviceModel1.setAverageRating(4.5);
    serviceModel1.setRatingCount(50);

    ServiceModel serviceModel2 = new ServiceModel();
    serviceModel2.setId(2);
    serviceModel2.setName("Service 2");
    serviceModel2.setDescription("Description of Service 2");
    serviceModel2.setPrice(150.0);
    serviceModel2.setAverageRating(4.0);
    serviceModel2.setRatingCount(30);

    // Create a PymeDTO instance and set properties
    PymeDTO pymeDTO = new PymeDTO();
    pymeDTO.setId(1);
    pymeDTO.setPymePostalCode("12345");
    pymeDTO.setPymePhone("123-456-7890");
    pymeDTO.setPymeName("My Pyme");
    pymeDTO.setPymeDescription("Description of My Pyme");
    pymeDTO.setServices(Arrays.asList(serviceModel1, serviceModel2));

    // Assert getters
    assertEquals(1, pymeDTO.getId());
    assertEquals("12345", pymeDTO.getPymePostalCode());
    assertEquals("123-456-7890", pymeDTO.getPymePhone());
    assertEquals("My Pyme", pymeDTO.getPymeName());
    assertEquals("Description of My Pyme", pymeDTO.getPymeDescription());
    assertEquals(Arrays.asList(serviceModel1, serviceModel2), pymeDTO.getServices());
  }
}
