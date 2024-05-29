package com.example.bicisapi.repository;

import com.example.bicisapi.domain.Aparcamiento;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AparcamientoRepository extends JpaRepository<Aparcamiento, Long> {

  Optional<Aparcamiento> findById(String id);

  @Query(value = "SELECT *, " +
      " (6371 * acos(cos(radians(:lat)) * cos(radians(latitude)) * cos(radians(longitude) - radians(:lon)) + sin(radians(:lat)) * sin(radians(latitude)))) AS distance " +
      " FROM Parking " +
      " ORDER BY distance ASC", 
      nativeQuery = true)
    List<Aparcamiento> findNearestAparcamientos(@Param("lat") double lat, @Param("lon") double lon);
}
