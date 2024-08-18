package com.example.servicesapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceModelTest {

    private ServiceModel service;

    @BeforeEach
    public void setUp() {
        Pyme pyme = new Pyme();
        pyme.setId(1);


        service = new ServiceModel();
        service.setId(1);
        service.setName("Test Service");
        service.setDescription("Test Description");
        service.setPrice(100.0);
        service.setPymeId(pyme.getId());
    }

    @Test
    public void testAddRating() {
        service.addRating(4.0);
        assertEquals(4.0, service.getTotalRating());
        assertEquals(1, service.getRatingCount());
        assertEquals(4.0, service.getAverageRating());

        service.setTotalRating(4);
        service.setRatingCount(1);
        service.setAverageRating(4);
        service.addRating(5.0);
        
        assertEquals(9.0, service.getTotalRating());
        assertEquals(2, service.getRatingCount());
        assertEquals(4.5, service.getAverageRating());
    }

    @Test
    public void testUpdateRating() {
        service.addRating(4.0);
        service.updateRating(4.0, 5.0);
        assertEquals(5.0, service.getTotalRating());
        assertEquals(1, service.getRatingCount());
        assertEquals(5.0, service.getAverageRating());
    }

    @Test
    public void getData() {
        assertEquals(1, service.getId());
        assertEquals("Test Service", service.getName());
        assertEquals("Test Description", service.getDescription());
        assertEquals(100.0, service.getPrice());
        assertEquals(1, service.getPymeId());
    }
}
