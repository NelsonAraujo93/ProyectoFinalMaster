package com.example.servicesapi.dto;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PymeProfileDTOTest {

  @Test
  public void testGettersAndSetters() {
    // Create ServiceDTO instances with sample data
    ServiceDTO serviceDTO1 = new ServiceDTO();
    serviceDTO1.setId(1);
    serviceDTO1.setName("Service 1");
    serviceDTO1.setDescription("Description of Service 1");
    serviceDTO1.setPrice(99.99);
    serviceDTO1.setAverageRating(4.7);
    serviceDTO1.setRatingCount(100);
    serviceDTO1.setPymeId(10);
    serviceDTO1.setPymeName("Pyme Name");
    serviceDTO1.setPymeDescription("Description of Pyme");
    serviceDTO1.setPymePhone("123-456-7890");
    serviceDTO1.setPymePostalCode("12345");

    ServiceDTO serviceDTO2 = new ServiceDTO();
    serviceDTO2.setId(2);
    serviceDTO2.setName("Service 2");
    serviceDTO2.setDescription("Description of Service 2");
    serviceDTO2.setPrice(149.99);
    serviceDTO2.setAverageRating(4.2);
    serviceDTO2.setRatingCount(80);
    serviceDTO2.setPymeId(10);
    serviceDTO2.setPymeName("Pyme Name");
    serviceDTO2.setPymeDescription("Description of Pyme");
    serviceDTO2.setPymePhone("123-456-7890");
    serviceDTO2.setPymePostalCode("12345");

    // Create a PymeProfileDTO instance and set properties
    PymeProfileDTO pymeProfileDTO = new PymeProfileDTO();
    pymeProfileDTO.setPymeName("My Pyme");
    pymeProfileDTO.setPymeDescription("Description of My Pyme");
    pymeProfileDTO.setPymePhone("987-654-3210");
    pymeProfileDTO.setPymePostalCode("54321");
    pymeProfileDTO.setServices(Arrays.asList(serviceDTO1, serviceDTO2));

    // Assert getters
    assertEquals("My Pyme", pymeProfileDTO.getPymeName());
    assertEquals("Description of My Pyme", pymeProfileDTO.getPymeDescription());
    assertEquals("987-654-3210", pymeProfileDTO.getPymePhone());
    assertEquals("54321", pymeProfileDTO.getPymePostalCode());
    assertEquals(Arrays.asList(serviceDTO1, serviceDTO2), pymeProfileDTO.getServices());
  }
}
