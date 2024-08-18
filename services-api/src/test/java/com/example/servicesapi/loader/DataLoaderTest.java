package com.example.servicesapi.loader;

import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.model.ServiceRequest;
import com.example.servicesapi.service.ServiceRequestService;
import com.example.servicesapi.model.User;
import com.example.servicesapi.repository.ServiceRepository;
import com.example.servicesapi.repository.ServiceRequestRepository;
import com.example.servicesapi.repository.PymeRepository;
import com.example.servicesapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class DataLoaderTest {

    @Mock
    private PymeRepository pymeRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private UserService userService;

    @Mock
    private ServiceRequestService serviceRequestService;

    @InjectMocks
    private DataLoader dataLoader;

    public DataLoaderTest() {
        MockitoAnnotations.openMocks(this);
    }

   @Test
    void testLoadData() {
        List<Pyme> mockedListOfPymes = new ArrayList<>();
        List<User> mockedListOfClients = new ArrayList<>();
        List<ServiceModel> mockedListOfServices = new ArrayList<>();

        // Populate lists with sample data
        Pyme pyme1 = new Pyme(); // initialize with necessary fields
        pyme1.setId(1);
        mockedListOfPymes.add(pyme1);

        User client1 = new User(); // initialize with necessary fields
        client1.setId(1);
        mockedListOfClients.add(client1);

        ServiceModel service1 = new ServiceModel(); // initialize with necessary fields
        service1.setId(1);
        mockedListOfServices.add(service1);

        when(pymeRepository.findAll()).thenReturn(mockedListOfPymes);
        when(userService.getAllClients()).thenReturn(mockedListOfClients);
        when(serviceRepository.findAll()).thenReturn(mockedListOfServices);

        dataLoader.loadData();

        // Verify interactions with mocks
        verify(serviceRepository, times(2)).save(any(ServiceModel.class)); // Adjust as needed
        verify(serviceRequestRepository, times(mockedListOfClients.size() * mockedListOfServices.size())).save(any(ServiceRequest.class));
    }

    @Test
    void testServiceRequestCreation() {
        // Initialize test data
        Date date = new Date();
        ServiceRequest serviceRequest1 = new ServiceRequest();
        serviceRequest1.setId("1");
        serviceRequest1.setServiceName("Service 1");
        serviceRequest1.setServiceId(1);
        serviceRequest1.setClientId(1);
        serviceRequest1.setRequestDate(date);
        serviceRequest1.setStatus("Pending");
        serviceRequest1.setDetails("Details for Service 1");
        serviceRequest1.setComment("hola");
        serviceRequest1.setRating(5.0);
        serviceRequest1.setRatingDate(date);


        // Call the method to test
        serviceRequestService.createServiceRequest(serviceRequest1);

        assertTrue(serviceRequest1.getId().equals("1"));
        assertTrue(serviceRequest1.getServiceName().equals("Service 1"));
        assertTrue(serviceRequest1.getServiceId() == 1);
        assertTrue(serviceRequest1.getClientId() == 1);
        assertTrue(serviceRequest1.getStatus().equals("Pending"));
        assertTrue(serviceRequest1.getDetails().equals("Details for Service 1"));
        assertTrue(serviceRequest1.getRequestDate().equals(date));
        assertTrue(serviceRequest1.getComment().equals("hola"));
        assertTrue(serviceRequest1.getRating() == 5.0);
        assertTrue(serviceRequest1.getRatingDate().equals(date));
    }
}
