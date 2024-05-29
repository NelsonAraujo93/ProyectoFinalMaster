package com.example.ayuntamientoapi.repository;

import com.example.ayuntamientoapi.domain.AggregatedData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AggregatedDataRepository extends MongoRepository<AggregatedData, String> {
    List<AggregatedData> findByTimestampBetween(Instant start, Instant end);

    Optional<AggregatedData> findTopByOrderByTimestampDesc();
}
