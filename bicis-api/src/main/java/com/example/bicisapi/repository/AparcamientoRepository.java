package com.example.bicisapi.repository;

import com.example.bicisapi.domain.Aparcamiento;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AparcamientoRepository extends JpaRepository<Aparcamiento, Long> {

  Optional<Aparcamiento> findById(String id);
}
