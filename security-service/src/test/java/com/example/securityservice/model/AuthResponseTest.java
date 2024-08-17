package com.example.securityservice.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthResponseTest {

    @Test
    public void testConstructorWithAllFields() {
        Long userId = 1L;
        String username = "testUser";
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        boolean isTokenBlacklisted = true;

        AuthResponse authResponse = new AuthResponse(userId, username, roles, isTokenBlacklisted);

        assertEquals(userId, authResponse.getUserId());
        assertEquals(username, authResponse.getUsername());
        assertEquals(roles, authResponse.getRoles());
        assertTrue(authResponse.isTokenBlacklisted());
    }

    @Test
    public void testConstructorWithMessage() {
        String message = "Error message";
        boolean isTokenBlacklisted = false;

        AuthResponse authResponse = new AuthResponse(message, isTokenBlacklisted);

        assertEquals(message, authResponse.getUsername()); // Note: Using 'username' to store message
        assertEquals(isTokenBlacklisted, authResponse.isTokenBlacklisted());
        assertEquals(null, authResponse.getUserId()); // userId should be null
        assertEquals(null, authResponse.getRoles()); // roles should be null
    }

    @Test
    public void testGettersAndSetters() {
        AuthResponse authResponse = new AuthResponse(null, null, null, false);

        // Set values
        authResponse.setUserId(2L);
        authResponse.setUsername("newUser");
        authResponse.setRoles(Arrays.asList("ROLE_USER"));
        authResponse.setTokenBlacklisted(true);

        // Verify values
        assertEquals(2L, authResponse.getUserId());
        assertEquals("newUser", authResponse.getUsername());
        assertEquals(Arrays.asList("ROLE_USER"), authResponse.getRoles());
        assertTrue(authResponse.isTokenBlacklisted());
    }
}
