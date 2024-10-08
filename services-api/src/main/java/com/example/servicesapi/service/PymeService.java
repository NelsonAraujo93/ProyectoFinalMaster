package com.example.servicesapi.service;

import com.example.servicesapi.dto.PymeDTO;
import com.example.servicesapi.model.Pyme;
import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.repository.PymeRepository;
import com.example.servicesapi.repository.ServiceRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PymeService {

    @Autowired
    private PymeRepository pymeRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Transactional
    public Pyme getPymeByUserId(Integer userId) {
        return pymeRepository.findByUserId(userId).orElse(null);
    }

    @Transactional
    public Optional<Pyme> getPymeById(Integer id) {
        return pymeRepository.findById(id);
    }

     public Optional<PymeDTO> getPymeDTOById(Integer id) {
        Optional<Pyme> pymeOptional = pymeRepository.findById(id);
        if (pymeOptional.isPresent()) {
            List<ServiceModel> services = serviceRepository.findAllByPymeId(id)
                    .stream()
                    .collect(Collectors.toList());

            PymeDTO pymeDTO = convertToPymeDTO(pymeOptional.get());
            pymeDTO.setServices(services);
            return Optional.of(pymeDTO);
        }
        return Optional.empty();
    }

    private PymeDTO convertToPymeDTO(Pyme pyme) {
        PymeDTO dto = new PymeDTO();
        dto.setId(pyme.getId());
        dto.setPymePostalCode(pyme.getPymePostalCode());
        dto.setPymePhone(pyme.getPymePhone());
        dto.setPymeName(pyme.getPymeName());
        dto.setPymeDescription(pyme.getPymeDescription());
        return dto;
    }
}
