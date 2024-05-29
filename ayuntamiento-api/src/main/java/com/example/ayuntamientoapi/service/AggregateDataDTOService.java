package com.example.ayuntamientoapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ayuntamientoapi.dao.AggregatedDataDTO;
import com.example.ayuntamientoapi.repository.AggregatedDataDTORepository;

@Service
public class AggregateDataDTOService {

  @Autowired
  private AggregatedDataDTORepository repository;

  public Optional<AggregatedDataDTO> getLastAggregatedData() {
        return repository.findTopByOrderByTimeStampDesc();
    }
}
