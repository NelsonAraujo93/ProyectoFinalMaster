package com.example.servicesapi.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PymeTest {

    @Test
    public void testGettersAndSetters() {
        Pyme pyme = new Pyme();

        // Set values
        pyme.setId(1);
        pyme.setPymePostalCode("12345");
        pyme.setPymePhone("555-1234");
        pyme.setPymeDescription("Test Pyme Description");

        // Assert values are set correctly
        assertEquals(1, pyme.getId(), "ID should match.");
        assertEquals("12345", pyme.getPymePostalCode(), "Postal Code should match.");
        assertEquals("555-1234", pyme.getPymePhone(), "Phone number should match.");
        assertEquals("Test Pyme Description", pyme.getPymeDescription(), "Description should match.");
    }

    @Test
    public void testSetters() {
        Pyme pyme = new Pyme();
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPass");


        // Set values using setters
        pyme.setId(2);
        pyme.setPymeName("Pyme Name");
        pyme.setPymePostalCode("67890");
        pyme.setPymePhone("555-5678");
        pyme.setPymeDescription("Another Pyme Description");
        pyme.setUser(user);

        // Verify values using getters
        assertEquals(user, pyme.getUser(), "User should match after setting.");
        assertEquals("Pyme Name", pyme.getPymeName(), "Name should match after setting.");
        assertEquals(2, pyme.getId(), "ID should match after setting.");
        assertEquals("67890", pyme.getPymePostalCode(), "Postal Code should match after setting.");
        assertEquals("555-5678", pyme.getPymePhone(), "Phone number should match after setting.");
        assertEquals("Another Pyme Description", pyme.getPymeDescription(), "Description should match after setting.");
    }
}
