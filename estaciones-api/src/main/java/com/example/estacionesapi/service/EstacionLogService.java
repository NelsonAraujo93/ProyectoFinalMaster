package com.example.estacionesapi.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.estacionesapi.domain.EstacionLog;
import com.example.estacionesapi.repository.EstacionLogRepository;

@Service
public class EstacionLogService {
  
  @Autowired
  private EstacionLogRepository estacionLogRepository;

  public List<EstacionLog> findTopByEstacionIdOrderByTimestampDesc(String estacionId) {
    return estacionLogRepository.findTopByEstacionIdOrderByTimestampDesc(estacionId);
  }

  public List<EstacionLog> findByEstacionIdAndTimestampBetween(String estacionId, Instant from, Instant to) {
    return estacionLogRepository.findByEstacionIdAndTimestampBetween(estacionId, from, to);
  }

  public EstacionLog createLog(String estacionId, EstacionLog estacionLog) {
    estacionLog.setTimestamp(Instant.now());
    estacionLog.setEstacionId(estacionId);
    return estacionLogRepository.save(estacionLog);
  }
}
