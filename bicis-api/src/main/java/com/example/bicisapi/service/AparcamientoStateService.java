package com.example.bicisapi.service;

import com.example.bicisapi.domain.AparcamientoState;
import com.example.bicisapi.repository.AparcamientoStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AparcamientoStateService {

    @Autowired
    private AparcamientoStateRepository aparcamientoStateRepository;

    public List<AparcamientoState> findByAparcamientoIdAndTimestampBetween(String aparcamientoId, Instant start, Instant end) {
        return aparcamientoStateRepository.findByAparcamientoIdAndTimestampBetween(aparcamientoId, start, end);
    }

    public AparcamientoState save(AparcamientoState aparcamientoState) {
        return aparcamientoStateRepository.save(aparcamientoState);
    }

    public List<AparcamientoState> findByAparcamientoId(String aparcamientoId) {
        return aparcamientoStateRepository.findByAparcamientoId(aparcamientoId);
    }

    public List<AparcamientoState> findTop10ByTimeStamp() {
        return aparcamientoStateRepository.findTop10ByTimestamp();
    }
}
