package com.example.services.repository;

import com.example.services.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Servicio, Long> {
}
