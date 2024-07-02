package com.example.services.service;

import com.example.services.model.Servicio;
import com.example.services.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<Servicio> getAllServices() {
        return serviceRepository.findAll();
    }

    public Servicio getServiceById(Long id) {
        return serviceRepository.findById(id).orElse(null);
    }

    public Servicio createService(Servicio service) {
        return serviceRepository.save(service);
    }

    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }
}
