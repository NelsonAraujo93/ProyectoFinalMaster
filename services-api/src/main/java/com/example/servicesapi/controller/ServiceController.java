package com.example.servicesapi.controller;

import com.example.servicesapi.dto.ServiceDTO;
import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.repository.PymeRepository;
import com.example.servicesapi.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private PymeRepository pymeRepository;

   @PostMapping
    public ResponseEntity<ServiceDTO> createService(
            @RequestBody ServiceModel service,
            @RequestHeader("x-auth-user-id") Long userId) {
        Optional<Pyme> pymeOptional = pymeRepository.findByUserId(userId);
        if (!pymeOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        service.setPyme(pymeOptional.get());
        ServiceModel createdService = serviceService.createService(service);

        // Convert ServiceModel to ServiceDTO
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(createdService.getId());
        serviceDTO.setName(createdService.getName());
        serviceDTO.setDescription(createdService.getDescription());
        serviceDTO.setPrice(createdService.getPrice());
        serviceDTO.setPymeId(createdService.getPyme().getId());

        return ResponseEntity.ok(serviceDTO);
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
        ServiceModel updatedService = serviceService.updateService(service, userId);
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
