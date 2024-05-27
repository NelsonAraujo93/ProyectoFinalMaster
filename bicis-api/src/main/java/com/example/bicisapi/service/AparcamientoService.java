package com.example.bicisapi.service;

import com.example.bicisapi.domain.Aparcamiento;
import com.example.bicisapi.repository.AparcamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AparcamientoService {

    @Autowired
    private AparcamientoRepository aparcamientoRepository;

    public List<Aparcamiento> findAll() {
        return aparcamientoRepository.findAll();
    }

    public Aparcamiento save(Aparcamiento aparcamiento) {
        return aparcamientoRepository.save(aparcamiento);
    }
}
