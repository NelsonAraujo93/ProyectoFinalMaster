package com.example.bicisapi.repository;

import com.example.bicisapi.domain.AparcamientoState;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AparcamientoStateRepository extends MongoRepository<AparcamientoState, String> {
  List<AparcamientoState> findByAparcamientoIdAndTimestampBetween(String aparcamientoId, Instant start, Instant end);

  List<AparcamientoState> findByAparcamientoId(String aparcamientoId);

  List<AparcamientoState> findTop10ByTimestamp();

  List<AparcamientoState> findTopByAparcamientoIdOrderByTimestampDesc(String aparcamientoId);

   @Aggregation(pipeline = {
          "{ $sort: { timestamp: -1 } }",
          "{ $group: { _id: '$aparcamientoId', latestState: { $first: '$$ROOT' } } }",
          "{ $replaceRoot: { newRoot: '$latestState' } }",
          "{ $sort: { bikesAvailable: -1 } }",
          "{ $limit: 10 }"
  })
  List<AparcamientoState> findTop10ByBikesAvailable();
}
