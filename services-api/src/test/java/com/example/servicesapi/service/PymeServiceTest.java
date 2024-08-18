package com.example.servicesapi.service;

import com.example.servicesapi.dto.PymeDTO;
import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.repository.PymeRepository;
import com.example.servicesapi.repository.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class PymeServiceTest {

    @InjectMocks
    private PymeService pymeService;

    @Mock
    private PymeRepository pymeRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPymeByUserId() {
        Integer userId = 1;
        Pyme pyme = new Pyme();
        pyme.setId(1);
        pyme.setPymeName("Test Pyme");

        when(pymeRepository.findByUserId(userId)).thenReturn(Optional.of(pyme));

        Pyme result = pymeService.getPymeByUserId(userId);

        assertNotNull(result);
        assertEquals("Test Pyme", result.getPymeName());
        
        verify(pymeRepository).findByUserId(userId);
    }

    @Test
    void testGetPymeById() {
        Integer id = 1;
        Pyme pyme = new Pyme();
        pyme.setId(id);
        pyme.setPymeName("Test Pyme");

        when(pymeRepository.findById(id)).thenReturn(Optional.of(pyme));

        Optional<Pyme> result = pymeService.getPymeById(id);

        assertTrue(result.isPresent());
        assertEquals("Test Pyme", result.get().getPymeName());
        
        verify(pymeRepository).findById(id);
    }

    @Test
    void testGetPymeDTOById() {
        Integer id = 1;
        Pyme pyme = new Pyme();
        pyme.setId(id);
        pyme.setPymeName("Test Pyme");
        pyme.setPymePostalCode("12345");
        pyme.setPymePhone("123-456-7890");
        pyme.setPymeDescription("Description");

        ServiceModel service1 = new ServiceModel();
        service1.setId(1);
        service1.setName("Service1");

        ServiceModel service2 = new ServiceModel();
        service2.setId(2);
        service2.setName("Service2");

        List<ServiceModel> services = Arrays.asList(service1, service2);

        when(pymeRepository.findById(id)).thenReturn(Optional.of(pyme));
        when(serviceRepository.findAllByPymeId(id)).thenReturn(services);

        Optional<PymeDTO> result = pymeService.getPymeDTOById(id);

        assertTrue(result.isPresent());
        PymeDTO pymeDTO = result.get();
        assertEquals("Test Pyme", pymeDTO.getPymeName());
        assertEquals("12345", pymeDTO.getPymePostalCode());
        assertEquals("123-456-7890", pymeDTO.getPymePhone());
        assertEquals("Description", pymeDTO.getPymeDescription());
        assertEquals(2, pymeDTO.getServices().size());
        
        verify(pymeRepository).findById(id);
        verify(serviceRepository).findAllByPymeId(id);
    }

    @Test
    void testGetPymeDTOById_NotFound() {
        Integer id = 1;

        when(pymeRepository.findById(id)).thenReturn(Optional.empty());

        Optional<PymeDTO> result = pymeService.getPymeDTOById(id);

        assertFalse(result.isPresent());

        verify(pymeRepository).findById(id);
        verify(serviceRepository, never()).findAllByPymeId(anyInt());
    }
}
