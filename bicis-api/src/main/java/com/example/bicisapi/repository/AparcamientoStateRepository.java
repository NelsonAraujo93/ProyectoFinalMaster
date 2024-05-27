package com.example.bicisapi.repository;

import com.example.bicisapi.domain.AparcamientoState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AparcamientoStateRepository extends MongoRepository<AparcamientoState, String> {
    List<AparcamientoState> findByAparcamientoIdAndTimestampBetween(String aparcamientoId, Instant start, Instant end);

    List<AparcamientoState> findByAparcamientoId(String aparcamientoId);

    List<AparcamientoState> findTop10ByTimestamp();
}
