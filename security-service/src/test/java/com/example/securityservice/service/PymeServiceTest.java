package com.example.securityservice.service;

import com.example.securityservice.model.Pyme;
import com.example.securityservice.model.User;
import com.example.securityservice.repository.PymeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PymeServiceTest {

    @InjectMocks
    private PymeService pymeService;

    @Mock
    private PymeRepository pymeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPymeByUserId_Found() {
        // Given
        Long userId = 1L;
        Pyme pyme = new Pyme();

        User user = new User();
        user.setId(userId);
        pyme.setUser(user);
        pyme.setPymeName("Test Pyme");

        when(pymeRepository.findByUserId(userId)).thenReturn(Optional.of(pyme));

        // When
        Pyme result = pymeService.getPymeByUserId(userId);

        // Then
        assertNotNull(result);
        assertEquals("Test Pyme", result.getPymeName());
        verify(pymeRepository).findByUserId(userId);
    }

    @Test
    void testGetPymeByUserId_NotFound() {
        // Given
        Long userId = 1L;

        when(pymeRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // When
        Pyme result = pymeService.getPymeByUserId(userId);

        // Then
        assertNull(result);
        verify(pymeRepository).findByUserId(userId);
    }
}
