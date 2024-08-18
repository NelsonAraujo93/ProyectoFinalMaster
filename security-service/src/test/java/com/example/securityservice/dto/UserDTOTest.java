package com.example.securityservice.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

public class UserDTOTest {

    @Test
    public void testGettersAndSetters() {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(1L);
        userDTO.setUsername("testUser");
        userDTO.setPassword("testPass");
        userDTO.setDni("12345678");
        userDTO.setPostalCode(12345);
        userDTO.setEnabled(true);
        userDTO.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));

        assertEquals(1L, userDTO.getId());
        assertEquals("testUser", userDTO.getUsername());
        assertEquals("testPass", userDTO.getPassword());
        assertEquals("12345678", userDTO.getDni());
        assertEquals(12345, userDTO.getPostalCode());
        assertTrue(userDTO.isEnabled());
        assertEquals(Arrays.asList("ROLE_USER", "ROLE_ADMIN"), userDTO.getRoles());
    }
}
