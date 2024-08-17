package com.example.securityservice.loader;

import com.example.securityservice.dto.PymeDTO;
import com.example.securityservice.dto.UserDTO;
import com.example.securityservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class DataLoaderTest {

    @InjectMocks
    private DataLoader dataLoader;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRun() throws Exception {
        // Given
        String[] args = {};

        // Mocking the behavior of the password encoder
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // When
        dataLoader.run(args);

        // Then
        verify(userService, times(4)).saveUser(any(UserDTO.class));
        verify(userService, times(4)).savePyme(any(PymeDTO.class));

        // Verify that the saveUser method was called with expected UserDTOs
        verify(userService).saveUser(argThat(userDTO -> 
            "client1".equals(userDTO.getUsername()) &&
            "DNI001".equals(userDTO.getDni()) &&
            12345 == userDTO.getPostalCode() &&
            "encodedPassword".equals(userDTO.getPassword()) &&
            userDTO.isEnabled() &&
            userDTO.getRoles().contains("CLIENT")
        ));

        // Verify that the savePyme method was called with expected PymeDTOs
        verify(userService).savePyme(argThat(pymeDTO -> 
            "pyme1".equals(pymeDTO.getUsername()) &&
            "DNI005".equals(pymeDTO.getDni()) &&
            12345 == pymeDTO.getPostalCode() &&
            "encodedPassword".equals(pymeDTO.getPassword()) &&
            pymeDTO.isEnabled() &&
            "12345".equals(pymeDTO.getPymePostalCode()) &&
            "555-0001".equals(pymeDTO.getPymePhone()) &&
            "Pyme 1".equals(pymeDTO.getPymeName()) &&
            "Description for Pyme 1".equals(pymeDTO.getPymeDescription())
        ));
    }

    @Test
    void testUserAlreadyExists() throws Exception {
        // Given
        String[] args = {};

        // Mock the behavior of existsByUsername to return true for any username
        when(userService.existsByUsername(anyString())).thenReturn(true);

        // When
        dataLoader.run(args);

        // Then
        // Verify that saveUser and savePyme methods are never called
        verify(userService, never()).saveUser(any(UserDTO.class));
        verify(userService, never()).savePyme(any(PymeDTO.class));
    }
}
