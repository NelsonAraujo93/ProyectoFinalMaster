package com.example.servicesapi.service;

import com.example.servicesapi.dto.ServiceRequestDTO;
import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.repository.PymeRepository;
import com.example.servicesapi.repository.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ServiceServiceTest {

    @InjectMocks
    private ServiceService serviceService;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private PymeRepository pymeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllServicesByUserId_Success() {
        Integer userId = 1;
        Pyme pyme = new Pyme();
        pyme.setId(1);
        List<ServiceModel> services = Arrays.asList(new ServiceModel(), new ServiceModel());

        when(pymeRepository.findByUserId(userId)).thenReturn(Optional.of(pyme));
        when(serviceRepository.findAllByPymeId(1)).thenReturn(services);

        List<ServiceModel> result = serviceService.getAllServicesByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(pymeRepository).findByUserId(userId);
        verify(serviceRepository).findAllByPymeId(1);
    }

    @Test
    void testGetAllServicesByUserId_PymeNotFound() {
        Integer userId = 1;

        when(pymeRepository.findByUserId(userId)).thenReturn(Optional.empty());

        List<ServiceModel> result = serviceService.getAllServicesByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(pymeRepository).findByUserId(userId);
        verify(serviceRepository, never()).findAllByPymeId(anyInt());
    }

    @Test
    void testGetServiceByIdAndUserId_Success() {
        Integer serviceId = 1;
        Integer userId = 1;
        Pyme pyme = new Pyme();
        pyme.setId(1);
        ServiceModel service = new ServiceModel();
        service.setId(1);

        when(pymeRepository.findByUserId(userId)).thenReturn(Optional.of(pyme));
        when(serviceRepository.findByIdAndPymeId(serviceId, pyme.getId())).thenReturn(Optional.of(service));

        Optional<ServiceModel> result = serviceService.getServiceByIdAndUserId(serviceId, userId);

        assertTrue(result.isPresent());
        assertEquals(serviceId, result.get().getId());

        verify(pymeRepository).findByUserId(userId);
        verify(serviceRepository).findByIdAndPymeId(serviceId, pyme.getId());
    }

    @Test
    void testCreateService() {
        ServiceModel service = new ServiceModel();

        when(serviceRepository.save(service)).thenReturn(service);

        ServiceModel result = serviceService.createService(service);

        assertNotNull(result);
        verify(serviceRepository).save(service);
    }
    
    @Test
    void testUpdateService_PymeNotFound() {
        Integer userId = 1;
        ServiceModel service = new ServiceModel();

        when(pymeRepository.findByUserId(userId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            serviceService.updateService(service, userId);
        });

        assertEquals("Pyme not found", thrown.getMessage());

        verify(pymeRepository).findByUserId(userId);
        verify(serviceRepository, never()).save(service);
    }

    @Test
    void testDeleteService() {
        Integer serviceId = 1;
        Integer userId = 1;

        doNothing().when(serviceRepository).deleteByIdAndPymeId(serviceId, userId);

        serviceService.deleteService(serviceId, userId);

        verify(serviceRepository).deleteByIdAndPymeId(serviceId, userId);
    }

    @Test
    void testGetServiceById() {
        Integer serviceId = 1;
        ServiceModel service = new ServiceModel();
        service.setId(serviceId);

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));

        Optional<ServiceModel> result = serviceService.getServiceById(serviceId);

        assertTrue(result.isPresent());
        assertEquals(serviceId, result.get().getId());

        verify(serviceRepository).findById(serviceId);
    }

    @Test
    void testFilterServices() {
        List<ServiceModel> services = Arrays.asList(
                new ServiceModel() {{ setPrice(100.0); setAverageRating(4.5); setName("Service A"); }},
                new ServiceModel() {{ setPrice(200.0); setAverageRating(3.5); setName("Service B"); }}
        );

        when(serviceRepository.findAll()).thenReturn(services);

        List<ServiceRequestDTO> result = serviceService.filterServices(100.0, 200.0, 4.0, "A");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Service A", result.get(0).getName());

        verify(serviceRepository).findAll();
    }

    @Test
    void testAddRatingToService() {
        Integer serviceId = 1;
        double rating = 5.0;
        ServiceModel service = new ServiceModel();
        service.setId(serviceId);

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
        when(serviceRepository.save(service)).thenReturn(service);

        ServiceModel result = serviceService.addRatingToService(serviceId, rating);

        assertNotNull(result);
        verify(serviceRepository).findById(serviceId);
        verify(serviceRepository).save(result);
    }

    @Test
    void testAddRatingToService_ServiceNotFound() {
        Integer serviceId = 1;
        double rating = 5.0;

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            serviceService.addRatingToService(serviceId, rating);
        });

        assertEquals("Service not found", thrown.getMessage());

        verify(serviceRepository).findById(serviceId);
        verify(serviceRepository, never()).save(any(ServiceModel.class));
    }

    @Test
    void testUpdateRatingForService() {
        Integer serviceId = 1;
        double oldRating = 4.0;
        double newRating = 5.0;
        ServiceModel service = new ServiceModel();
        service.setId(serviceId);

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
        when(serviceRepository.save(service)).thenReturn(service);

        ServiceModel result = serviceService.updateRatingForService(serviceId, oldRating, newRating);

        assertNotNull(result);
        verify(serviceRepository).findById(serviceId);
        verify(serviceRepository).save(result);
    }

    @Test
    void testUpdateRatingForService_ServiceNotFound() {
        Integer serviceId = 1;
        double oldRating = 4.0;
        double newRating = 5.0;

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            serviceService.updateRatingForService(serviceId, oldRating, newRating);
        });

        assertEquals("Service not found", thrown.getMessage());

        verify(serviceRepository).findById(serviceId);
        verify(serviceRepository, never()).save(any(ServiceModel.class));
    }
}
