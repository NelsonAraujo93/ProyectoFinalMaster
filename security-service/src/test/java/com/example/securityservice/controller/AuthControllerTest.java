package com.example.securityservice.controller;

import com.example.securityservice.dto.PymeDTO;
import com.example.securityservice.dto.UserDTO;
import com.example.securityservice.model.AuthRequest;
import com.example.securityservice.model.AuthResponse;
import com.example.securityservice.model.AuthResponseUser;
import com.example.securityservice.model.User;
import com.example.securityservice.model.UserRole;
import com.example.securityservice.service.BlacklistedTokenService;
import com.example.securityservice.service.JwtService;
import com.example.securityservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BlacklistedTokenService blacklistedTokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateSuccess() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("encodedpassword");
        user.setRoles(Collections.singleton(new UserRole(user, "ROLE_USER")));

        when(userService.findByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedpassword")).thenReturn(true);
        when(jwtService.generateToken(1L, "user", Collections.singletonList("ROLE_USER"))).thenReturn("token");

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("user");
        userDTO.setRoles(Collections.singletonList("ROLE_USER"));

        when(userService.convertToUserDto(user)).thenReturn(userDTO);

        AuthRequest authRequest = new AuthRequest("user", "password");
        ResponseEntity<AuthResponseUser> response = authController.authenticate(authRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthResponseUser responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("token", responseBody.getToken());
        assertNotNull(responseBody.getData());
        assertEquals("user", responseBody.getData().getUsername());
        assertFalse(responseBody.isTokenBlacklisted());
    }

    @Test
    void testAuthenticateFailure() {
        when(userService.findByUsername("user")).thenReturn(Optional.empty());

        AuthRequest authRequest = new AuthRequest("user", "password");
        ResponseEntity<AuthResponseUser> response = authController.authenticate(authRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        AuthResponseUser responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.isTokenBlacklisted());
        assertEquals("Invalid username or password", responseBody.getToken());
    }

    
    @SuppressWarnings("null")
    @Test
    void testRegisterClientSuccess() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("clientuser");
        userDTO.setPassword("password");

        when(userService.existsByUsername("clientuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedpassword");

        ResponseEntity<Object> response = authController.registerClient(userDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("User created successfully with role: CLIENT"));
        verify(userService).saveUser(any(UserDTO.class));
    }

    @SuppressWarnings("null")
    @Test
    void testRegisterClientUsernameAlreadyTaken() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("clientuser");

        when(userService.existsByUsername("clientuser")).thenReturn(true);

        ResponseEntity<Object> response = authController.registerClient(userDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Username is already taken"));
        verify(userService, never()).saveUser(any(UserDTO.class));
    }

    @SuppressWarnings("null")
    @Test
    void testRegisterPymeSuccess() {
        PymeDTO pymeDTO = new PymeDTO();
        pymeDTO.setUsername("pymeuser");
        pymeDTO.setPassword("password");

        when(userService.existsByUsername("pymeuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedpassword");

        ResponseEntity<Object> response = authController.registerPyme(pymeDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Pyme created successfully with role: PYME"));
        verify(userService).savePyme(any(PymeDTO.class));
    }

    @SuppressWarnings("null")
    @Test
    void testRegisterPymeUsernameAlreadyTaken() {
        PymeDTO pymeDTO = new PymeDTO();
        pymeDTO.setUsername("pymeuser");

        when(userService.existsByUsername("pymeuser")).thenReturn(true);

        ResponseEntity<Object> response = authController.registerPyme(pymeDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Username is already taken"));
        verify(userService, never()).savePyme(any(PymeDTO.class));
    }

    @Test
    void testValidateTokenSuccess() {
        String token = "validtoken";
        when(blacklistedTokenService.isTokenBlacklisted(token)).thenReturn(false);
        when(jwtService.validateToken(token)).thenReturn(true);
        when(jwtService.getUserIdFromToken(token)).thenReturn(1L);
        when(jwtService.getUsernameFromToken(token)).thenReturn("user");
        when(jwtService.getRolesFromToken(token)).thenReturn(Collections.singletonList("ROLE_USER"));

        ResponseEntity<AuthResponse> response = authController.validateToken("Bearer " + token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthResponse responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1L, responseBody.getUserId());
        assertEquals("user", responseBody.getUsername());
        assertFalse(responseBody.getUsername().contains("Invalid")); // Using username for error messages
    }

    @Test
    void testValidateTokenInvalidToken() {
        String token = "invalidtoken";
        when(blacklistedTokenService.isTokenBlacklisted(token)).thenReturn(false);
        when(jwtService.validateToken(token)).thenReturn(false);

        ResponseEntity<AuthResponse> response = authController.validateToken("Bearer " + token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        AuthResponse responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.getUsername().contains("Invalid")); // Using username for error messages
        assertEquals("Invalid token", responseBody.getUsername()); // Using username for error messages
    }

    @Test
    void testLogout() {
        String token = "logouttoken";
        ResponseEntity<String> response = authController.logout("Bearer " + token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logged out successfully", response.getBody());
        verify(blacklistedTokenService).blacklistToken(token);
    }
}
