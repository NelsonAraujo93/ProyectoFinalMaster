package com.example.ayuntamientoapi.config;

import com.example.ayuntamientoapi.service.AggregateDataDTOService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private AggregateDataDTOService ayuntamientoService;

    @Value("${aggregation.interval}")

    @Scheduled(fixedRateString = "${aggregation.interval}")
    public void aggregateDataTask() {
        ayuntamientoService.aggregateData();
    }
}
