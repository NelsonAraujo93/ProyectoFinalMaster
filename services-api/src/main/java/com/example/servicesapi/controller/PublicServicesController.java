package com.example.servicesapi.controller;

import com.example.servicesapi.dto.PymeDTO;
import com.example.servicesapi.dto.ServiceRequestDTO;
import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.repository.PymeRepository;
import com.example.servicesapi.repository.ServiceRepository;
import com.example.servicesapi.service.PymeService;
import com.example.servicesapi.service.ServiceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicServicesController {

    @Autowired
    private PymeService pymeService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private PymeRepository pymeRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("/pymes")
    public ResponseEntity<List<Pyme>> getAllPymes() {
        List<Pyme> pymes = pymeRepository.findAll();
        return ResponseEntity.ok(pymes);
    }

    @GetMapping("/services")
    public ResponseEntity<List<ServiceModel>> getAllServices() {
        List<ServiceModel> services = serviceRepository.findAll();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/pymes/latest")
    public ResponseEntity<List<Pyme>> getLastCreatedPymes() {
        List<Pyme> pymes = pymeRepository.findTop6ByOrderByIdDesc();
        return ResponseEntity.ok(pymes);
    }

    @GetMapping("/services/latest")
    public ResponseEntity<List<ServiceModel>> getLastCreatedServices() {
        List<ServiceModel> services = serviceRepository.findTop6ByOrderByIdDesc();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/pymes/{id}")
    public ResponseEntity<PymeDTO> getPymeById(@PathVariable Long id) {
        return pymeService.getPymeDTOById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/services/{id}")
    public ResponseEntity<ServiceModel> getServiceById(@PathVariable Long id) {
        return serviceService.getServiceById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // New Endpoint: Filter services by price, rating, and name
    @GetMapping("/services/filter")
    public ResponseEntity<List<ServiceRequestDTO>> filterServices(
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "name", required = false) String name) {

        List<ServiceRequestDTO> filteredServices = serviceService.filterServices(minPrice, maxPrice, minRating, name);
        return ResponseEntity.ok(filteredServices);
    }
}
