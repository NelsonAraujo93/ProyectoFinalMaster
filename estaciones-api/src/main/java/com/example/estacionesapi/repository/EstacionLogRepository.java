package com.example.estacionesapi.repository;

import com.example.estacionesapi.domain.EstacionLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface EstacionLogRepository extends MongoRepository<EstacionLog, String> {
    List<EstacionLog> findByEstacionIdAndTimestampBetween(String estacionId, Instant start, Instant end);
    List<EstacionLog> findTopByEstacionIdOrderByTimestampDesc(String estacionId);
    List<EstacionLog> findByEstacionId(String estacionId);
}
