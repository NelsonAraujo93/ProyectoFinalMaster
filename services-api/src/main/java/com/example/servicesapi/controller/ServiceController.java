package com.example.servicesapi.controller;

import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @PostMapping
    public ResponseEntity<ServiceModel> createService(
            @RequestBody ServiceModel service,
            @RequestHeader("x-auth-user-id") Long userId) {
        service.setUserId(userId);
        ServiceModel createdService = serviceService.createService(service);
        return ResponseEntity.ok(createdService);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceModel> getServiceById(
            @PathVariable Long id,
            @RequestHeader("x-auth-user-id") Long userId) {
        Optional<ServiceModel> service = serviceService.getServiceByIdAndUserId(id, userId);
        return service.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ServiceModel>> getAllServices(
            @RequestHeader("x-auth-user-id") Long userId) {
        List<ServiceModel> services = serviceService.getAllServicesByUserId(userId);
        return ResponseEntity.ok(services);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceModel> updateService(
            @PathVariable Long id,
            @RequestBody ServiceModel service,
            @RequestHeader("x-auth-user-id") Long userId) {
        service.setId(id);
        service.setUserId(userId);
        ServiceModel updatedService = serviceService.updateService(service);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(
            @PathVariable Long id,
            @RequestHeader("x-auth-user-id") Long userId) {
        serviceService.deleteService(id, userId);
        return ResponseEntity.noContent().build();
    }
}
