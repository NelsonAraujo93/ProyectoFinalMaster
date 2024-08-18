package com.example.servicesapi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FinalizeRequestDTOTest {

  @Test
  public void testGettersAndSetters() {
    // Create an instance of FinalizeRequestDTO
    FinalizeRequestDTO finalizeRequestDTO = new FinalizeRequestDTO();

    // Set properties
    finalizeRequestDTO.setRating(5);
    finalizeRequestDTO.setComment("Excellent service");

    // Assert getters
    assertEquals(5, finalizeRequestDTO.getRating());
    assertEquals("Excellent service", finalizeRequestDTO.getComment());
  }
}
