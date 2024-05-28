package com.example.estacionesapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.estacionesapi.domain.Estacion;
import com.example.estacionesapi.repository.EstacionRepository;

@Service
public class EstacionService {
  
  @Autowired
  private EstacionRepository estacionRepository;

  public List<Estacion> findAll() {
    return estacionRepository.findAll();
  }

  public Optional<Estacion> findById(String id) {
    return estacionRepository.findById(id);
  }

  public Estacion save(Estacion estacion) {
    return estacionRepository.save(estacion);
  }

  public void deleteById(Long id) {
    estacionRepository.deleteById(id);
  }

}
