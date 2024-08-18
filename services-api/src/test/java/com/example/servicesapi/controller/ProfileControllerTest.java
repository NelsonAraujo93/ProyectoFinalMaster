package com.example.servicesapi.controller;

import com.example.servicesapi.dto.PymeProfileDTO;
import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.repository.PymeRepository;
import com.example.servicesapi.service.ServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class ProfileControllerTest {

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private PymeRepository pymeRepository;

    @Mock
    private ServiceService serviceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testGetProfileSuccess() {
        // Setup mock data
        Pyme pyme = new Pyme();
        pyme.setPymeName("Test Pyme");
        pyme.setPymeDescription("Description");
        pyme.setPymePhone("123456789");
        pyme.setPymePostalCode("12345");

        when(pymeRepository.findByUserId(anyInt())).thenReturn(Optional.of(pyme));

        // Mock service method (adjust if service method is actually called)
        when(serviceService.getAllServicesByUserId(anyInt())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<PymeProfileDTO> response = profileController.getProfile(1);

        // Validate response
        assertEquals(200, response.getStatusCodeValue());
        PymeProfileDTO profileDTO = response.getBody();
        assertNotNull(profileDTO);
        assertEquals("Test Pyme", profileDTO.getPymeName());
        assertEquals("Description", profileDTO.getPymeDescription());
        assertEquals("123456789", profileDTO.getPymePhone());
        assertEquals("12345", profileDTO.getPymePostalCode());
    }

    @SuppressWarnings("deprecation")
    @Test
    void testGetProfileNotFound() {
        when(pymeRepository.findByUserId(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<PymeProfileDTO> response = profileController.getProfile(1);

        assertEquals(404, response.getStatusCodeValue());
    }
}
