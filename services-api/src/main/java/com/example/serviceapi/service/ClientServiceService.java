package com.example.services.service;

import com.example.services.model.ClientService;
import com.example.services.repository.ClientServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceService {

    @Autowired
    private ClientServiceRepository clientServiceRepository;

    public List<ClientService> getAllClientServices() {
        return clientServiceRepository.findAll();
    }

    public ClientService getClientServiceById(String id) {
        return clientServiceRepository.findById(id).orElse(null);
    }

    public List<ClientService> getClientServicesByServiceId(Long serviceId) {
        return clientServiceRepository.findByServiceId(serviceId);
    }

    public ClientService createClientService(ClientService clientService) {
        return clientServiceRepository.save(clientService);
    }

    public void deleteClientService(String id) {
        clientServiceRepository.deleteById(id);
    }
}
