package com.example.bicisapi.repositories;

import com.example.bicisapi.domain.Aparcamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AparcamientoRepository extends JpaRepository<Aparcamiento, Long> {
}