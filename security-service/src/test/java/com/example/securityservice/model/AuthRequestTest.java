package com.example.securityservice.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthRequestTest {

    @Test
    public void testGettersAndSetters() {
        AuthRequest authRequest = new AuthRequest();

        // Set values
        authRequest.setUsername("testUser");
        authRequest.setPassword("testPass");

        // Verify values
        assertEquals("testUser", authRequest.getUsername());
        assertEquals("testPass", authRequest.getPassword());
    }
}
