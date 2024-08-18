package com.example.securityservice.model;

import com.example.securityservice.dto.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

public class AuthResponseUserTest {

    @Test
    public void testConstructorWithAllFields() {
        String token = "sampleToken";
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("password");
        userDTO.setRoles(Arrays.asList("ROLE_USER"));
        boolean isTokenBlacklisted = true;

        AuthResponseUser authResponseUser = new AuthResponseUser(token, userDTO, isTokenBlacklisted);

        assertEquals(token, authResponseUser.getToken());
        assertEquals(userDTO, authResponseUser.getData());
        assertTrue(authResponseUser.isTokenBlacklisted());
    }

    @Test
    public void testConstructorWithMessage() {
        String message = "Error message";
        boolean isTokenBlacklisted = false;

        AuthResponseUser authResponseUser = new AuthResponseUser(message, isTokenBlacklisted);

        assertEquals(message, authResponseUser.getToken());
        assertEquals(null, authResponseUser.getData()); // data should be null
        assertTrue(!authResponseUser.isTokenBlacklisted());
    }

    @Test
    public void testGettersAndSetters() {
        AuthResponseUser authResponseUser = new AuthResponseUser(null, null, false);

        // Set values
        authResponseUser.setToken("newToken");
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newUser");
        authResponseUser.setData(userDTO);
        authResponseUser.setTokenBlacklisted(true);
        authResponseUser.setUserDTO(userDTO);

        // Verify values
        assertEquals("newToken", authResponseUser.getToken());
        assertEquals(userDTO, authResponseUser.getData());
        assertTrue(authResponseUser.isTokenBlacklisted());
        assertEquals(userDTO, authResponseUser.getUserDTO());
    }
}
