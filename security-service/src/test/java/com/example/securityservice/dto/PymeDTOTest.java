package com.example.securityservice.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

public class PymeDTOTest {

    @Test
    public void testGettersAndSetters() {
        PymeDTO pymeDTO = new PymeDTO();

        pymeDTO.setId(1L);
        pymeDTO.setUsername("testUser");
        pymeDTO.setPassword("testPass");
        pymeDTO.setDni("12345678");
        pymeDTO.setPostalCode(12345);
        pymeDTO.setEnabled(true);
        pymeDTO.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
        pymeDTO.setPymePostalCode("98765");
        pymeDTO.setPymePhone("123-456-7890");
        pymeDTO.setPymeName("TestPyme");
        pymeDTO.setPymeDescription("Test Description");

        assertEquals(1L, pymeDTO.getId());
        assertEquals("testUser", pymeDTO.getUsername());
        assertEquals("testPass", pymeDTO.getPassword());
        assertEquals("12345678", pymeDTO.getDni());
        assertEquals(12345, pymeDTO.getPostalCode());
        assertTrue(pymeDTO.isEnabled());
        assertEquals(Arrays.asList("ROLE_USER", "ROLE_ADMIN"), pymeDTO.getRoles());
        assertEquals("98765", pymeDTO.getPymePostalCode());
        assertEquals("123-456-7890", pymeDTO.getPymePhone());
        assertEquals("TestPyme", pymeDTO.getPymeName());
        assertEquals("Test Description", pymeDTO.getPymeDescription());
    }
}
