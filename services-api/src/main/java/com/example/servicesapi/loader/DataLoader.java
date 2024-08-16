package com.example.servicesapi.loader;

import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.model.ServiceRequest;
import com.example.servicesapi.model.User;
import com.example.servicesapi.repository.ServiceRepository;
import com.example.servicesapi.repository.ServiceRequestRepository;
import com.example.servicesapi.service.UserService;
import com.example.servicesapi.repository.PymeRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class DataLoader {

  private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

  @Autowired
  private PymeRepository pymeRepository;

  @Autowired
  private ServiceRepository serviceRepository;

  @Autowired
  private ServiceRequestRepository serviceRequestRepository;

  @Autowired
  private UserService userService;

  @PostConstruct
  public void loadData() {
    // Fetch Pymes
    List<Pyme> pymes = pymeRepository.findAll();

    if (pymes.isEmpty()) {
      logger.warn("No Pymes found! Services and requests cannot be created.");
      return;
    }

    logger.info("Found {} Pymes. Creating services and requests...", pymes.size());

    // Create services and associated requests for each Pyme
    for (int i = 0; i < pymes.size(); i++) {
      Pyme pyme = pymes.get(i);
      for (int j = 1; j <= 2; j++) { // Create 2 services for each Pyme
        ServiceModel service = new ServiceModel(
            "Service " + (i + 1) + "." + j,
            "Description for service " + (i + 1) + "." + j,
            100.0 + j * 50,
            pyme.getId());
        serviceRepository.save(service);
        logger.info("Created Service: {} for Pyme: {}", service.getName(), pyme.getPymeName());
      }
    }

    createRequests();
  }

  public void createRequests() {
    // Create service requests for each client and service combination
    List<User> clients = userService.getAllClients();

    if (clients.isEmpty()) {
      logger.warn("No clients found! Service requests cannot be created.");
      return;
    }

    logger.info("Found {} clients. Creating service requests...", clients.size());

    // Fetch all services (assuming you want to create requests for all services)
    List<ServiceModel> services = serviceRepository.findAll();

    if (services.isEmpty()) {
      logger.warn("No services found! Service requests cannot be created.");
      return;
    }

    for (User client : clients) {
      for (ServiceModel service : services) {
        ServiceRequest serviceRequest = new ServiceRequest(
            service.getName(),
            service.getId(),
            client.getId(),
            new Date(),
            "Pending",
            "Request details for client " + client.getUsername());
        serviceRequestRepository.save(serviceRequest);
        logger.info("Created ServiceRequest for client {} and service {}", client.getUsername(), service.getName());
      }
    }
  }
}
