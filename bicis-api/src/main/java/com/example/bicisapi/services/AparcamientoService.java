package com.example.bicisapi.services;

import com.example.bicisapi.domain.Aparcamiento;
import com.example.bicisapi.repositories.AparcamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class AparcamientoService {

    @Autowired
    private AparcamientoRepository ra;

    public Aparcamiento agregarAparcamiento(Aparcamiento aparcamiento) {
        return ra.save(aparcamiento);
    }

    public void eliminarAparcamiento(Long id) {
        ra.deleteById(id);
    }

    public Aparcamiento editarAparcamiento(Aparcamiento aparcamiento) {
        return ra.save(aparcamiento);
    }

    public List<Aparcamiento> obtenerTodosAparcamientos() {
        return ra.findAll();
    }
}
