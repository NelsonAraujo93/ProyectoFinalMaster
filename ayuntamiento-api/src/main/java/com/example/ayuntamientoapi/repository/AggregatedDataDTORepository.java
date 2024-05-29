package com.example.ayuntamientoapi.repository;

import com.example.ayuntamientoapi.dao.AggregatedDataDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AggregatedDataDTORepository extends MongoRepository<AggregatedDataDTO, String> {
    Optional<AggregatedDataDTO> findTopByOrderByTimeStampDesc();
}
