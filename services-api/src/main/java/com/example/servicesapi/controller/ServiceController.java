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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            @RequestHeader("x-auth-user-id") Integer userId) {
        // Check if a Pyme exists for the given userId
        Optional<Pyme> pymeOptional = pymeRepository.findByUserId(userId);
        if (!pymeOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 if Pyme doesn't exist
        }
    
        // Set the Pyme ID to the service
        service.setPymeId(pymeOptional.get().getId());
    
        // Create the service
        ServiceModel createdService = serviceService.createService(service);
    
        // Convert to DTO and return
        ServiceDTO serviceDTO = convertToDTO(createdService);
        return ResponseEntity.ok(serviceDTO);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getServiceById(
            @PathVariable Integer id,
            @RequestHeader("x-auth-user-id") Integer userId) {
        Optional<ServiceModel> service = serviceService.getServiceByIdAndUserId(id, userId);
        return service.map(this::convertToDTO)
                      .map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAllServices(
            @RequestHeader("x-auth-user-id") Integer userId) {
        
        Optional<Pyme> pyme = pymeRepository.findByUserId(userId);
        if (pyme.isPresent()) {
            List<ServiceModel> services = serviceService.getAllServicesByUserId(userId);
            List<ServiceDTO> serviceDTOs = services.stream()
                                               .map(this::convertToDTO)
                                               .collect(Collectors.toList());
            return ResponseEntity.ok(serviceDTOs);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceDTO> updateService(
            @PathVariable Integer id,
            @RequestBody ServiceModel service,
            @RequestHeader("x-auth-user-id") Integer userId) {
        service.setId(id);
        ServiceModel updatedService = serviceService.updateService(service, userId);
        ServiceDTO serviceDTO = convertToDTO(updatedService);
        return ResponseEntity.ok(serviceDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(
            @PathVariable Integer id,
            @RequestHeader("x-auth-user-id") Integer userId) {
        serviceService.deleteService(id, userId);
        return ResponseEntity.noContent().build();
    }

    private ServiceDTO convertToDTO(ServiceModel serviceModel) {
        Pyme pyme = pymeRepository.findById(serviceModel.getPymeId()).orElseThrow(() -> new RuntimeException("Pyme not found"));
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(serviceModel.getId());
        serviceDTO.setName(serviceModel.getName());
        serviceDTO.setDescription(serviceModel.getDescription());
        serviceDTO.setPrice(serviceModel.getPrice());
        serviceDTO.setAverageRating(serviceModel.getAverageRating());
        serviceDTO.setRatingCount(serviceModel.getRatingCount());
        serviceDTO.setPymeId(pyme.getId());
        serviceDTO.setPymeName(pyme.getPymeName());
        serviceDTO.setPymeDescription(pyme.getPymeDescription());
        serviceDTO.setPymePhone(pyme.getPymePhone());
        serviceDTO.setPymePostalCode(pyme.getPymePostalCode());

        return serviceDTO;
    }
}
