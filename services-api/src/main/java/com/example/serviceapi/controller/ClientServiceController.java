package com.example.services.controller;

import com.example.services.model.ClientService;
import com.example.services.service.ClientServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client-services")
public class ClientServiceController {

    @Autowired
    private ClientServiceService clientServiceService;

    @GetMapping
    public List<ClientService> getAllClientServices() {
        return clientServiceService.getAllClientServices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientService> getClientServiceById(@PathVariable String id) {
        ClientService clientService = clientServiceService.getClientServiceById(id);
        if (clientService != null) {
            return ResponseEntity.ok(clientService);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/service/{serviceId}")
    public List<ClientService> getClientServicesByServiceId(@PathVariable Long serviceId) {
        return clientServiceService.getClientServicesByServiceId(serviceId);
    }

    @PostMapping
    public ClientService createClientService(@RequestBody ClientService clientService) {
        return clientServiceService.createClientService(clientService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientService(@PathVariable String id) {
        clientServiceService.deleteClientService(id);
        return ResponseEntity.noContent().build();
    }
}
