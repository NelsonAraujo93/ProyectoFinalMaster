package com.example.estacionesapi.repository;

import com.example.estacionesapi.domain.Estacion;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstacionRepository extends JpaRepository<Estacion, Long> {

  Optional<Estacion> findById(String id);
}
