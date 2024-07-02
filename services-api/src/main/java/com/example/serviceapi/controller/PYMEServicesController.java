package com.example.services.controller;

import com.example.services.model.ClientService;
import com.example.services.service.ClientServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pymes-services")
public class PYMEServicesController {

    @Autowired
    private ClientServiceService clientServiceService;

    @GetMapping("/service/{serviceId}/requests")
    public List<ClientService> getRequestsForService(@PathVariable Long serviceId) {
        return clientServiceService.getClientServicesByServiceId(serviceId);
    }
}
