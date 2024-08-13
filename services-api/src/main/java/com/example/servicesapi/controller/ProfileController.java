package com.example.servicesapi.controller;

import com.example.servicesapi.dto.PymeProfileDTO;
import com.example.servicesapi.dto.ServiceDTO;
import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.repository.PymeRepository;
import com.example.servicesapi.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private PymeRepository pymeRepository;

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public ResponseEntity<PymeProfileDTO> getProfile(@RequestHeader("x-auth-user-id") Long userId) {
        Optional<Pyme> pymeOptional = pymeRepository.findByUserId(userId);
        if (!pymeOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Pyme pyme = pymeOptional.get();

        PymeProfileDTO profileDTO = new PymeProfileDTO();
        profileDTO.setPymeName(pyme.getPymeName());
        profileDTO.setPymeDescription(pyme.getPymeDescription());
        profileDTO.setPymePhone(pyme.getPymePhone());
        profileDTO.setPymePostalCode(pyme.getPymePostalCode());

        List<ServiceDTO> services = serviceService.getAllServicesByUserId(userId)
                .stream()
                .map(service -> {
                    ServiceDTO serviceDTO = new ServiceDTO();
                    serviceDTO.setId(service.getId());
                    serviceDTO.setName(service.getName());
                    serviceDTO.setDescription(service.getDescription());
                    serviceDTO.setPrice(service.getPrice());
                    serviceDTO.setPymeId(service.getPyme().getId());
                    return serviceDTO;
                })
                .collect(Collectors.toList());

        profileDTO.setServices(services);

        return ResponseEntity.ok(profileDTO);
    }
}
