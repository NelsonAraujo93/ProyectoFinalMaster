package com.example.servicesapi.controller;

import com.example.servicesapi.dto.PymeDTO;
import com.example.servicesapi.dto.ServiceDTO;
import com.example.servicesapi.dto.ServiceDetailsDTO;
import com.example.servicesapi.dto.ServiceRequestDTO;
import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.model.ServiceRequest;
import com.example.servicesapi.repository.PymeRepository;
import com.example.servicesapi.repository.ServiceRepository;
import com.example.servicesapi.repository.ServiceRequestRepository;
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
import java.util.stream.Collectors;

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

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @GetMapping("/pymes")
    public ResponseEntity<List<Pyme>> getAllPymes() {
        List<Pyme> pymes = pymeRepository.findAll();
        return ResponseEntity.ok(pymes);
    }

    @GetMapping("/services")
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        List<ServiceModel> services = serviceRepository.findAll();
        List<ServiceDTO> serviceDTOs = services.stream()
                                               .map(this::convertToDTO)
                                               .collect(Collectors.toList());
        return ResponseEntity.ok(serviceDTOs);
    }

    @GetMapping("/pymes/latest")
    public ResponseEntity<List<Pyme>> getLastCreatedPymes() {
        List<Pyme> pymes = pymeRepository.findTop6ByOrderByIdDesc();
        return ResponseEntity.ok(pymes);
    }

    @GetMapping("/services/latest")
    public ResponseEntity<List<ServiceDTO>> getLastCreatedServices() {
        List<ServiceModel> services = serviceRepository.findTop6ByOrderByIdDesc();
        List<ServiceDTO> serviceDTOs = services.stream()
                                               .map(this::convertToDTO)
                                               .collect(Collectors.toList());
        return ResponseEntity.ok(serviceDTOs);
    }

    @GetMapping("/pymes/{id}")
    public ResponseEntity<PymeDTO> getPymeById(@PathVariable Integer id) {
        return pymeService.getPymeDTOById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

     @GetMapping("/services/{id}")
    public ResponseEntity<ServiceDetailsDTO> getServiceById(@PathVariable Integer id) {
        return serviceService.getServiceById(id)
                .map(serviceModel -> {
                    ServiceDetailsDTO serviceDetailsDTO = new ServiceDetailsDTO();
                    serviceDetailsDTO.setService(convertToDTO(serviceModel));

                    List<ServiceRequest> lastThreeRequests = serviceRequestRepository
                            .findTop3ByServiceIdAndStatusOrderByRatingDateDesc(id, "Finalized");
                    serviceDetailsDTO.setRequests(lastThreeRequests);

                    return ResponseEntity.ok(serviceDetailsDTO);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/services/filter")
    public ResponseEntity<List<ServiceRequestDTO>> filterServices(
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "name", required = false) String name) {

        List<ServiceRequestDTO> filteredServices = serviceService.filterServices(minPrice, maxPrice, minRating, name);
        return ResponseEntity.ok(filteredServices);
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
