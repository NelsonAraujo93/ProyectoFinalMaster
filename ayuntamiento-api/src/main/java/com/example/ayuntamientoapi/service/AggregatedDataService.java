package com.example.ayuntamientoapi.service;

import com.example.ayuntamientoapi.dao.AggregatedDataDTO;
import com.example.ayuntamientoapi.dao.AggregatedDataDTO.AparcamientoDTO;
import com.example.ayuntamientoapi.dao.AggregatedDataDTO.AirQualityDTO;
import com.example.ayuntamientoapi.domain.AggregatedData;
import com.example.ayuntamientoapi.repository.AggregatedDataDTORepository;
import com.example.ayuntamientoapi.repository.AggregatedDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AggregatedDataService {

    @Autowired
    private AggregatedDataRepository repository;

    @Autowired
    private AggregatedDataDTORepository DTOrepo;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${api.bicis.url}")
    private String bicisUrl;

    @Value("${api.estaciones.url}")
    private String estacionesUrl;

    public Optional<AggregatedData> getLastAggregatedData() {
        return repository.findTopByOrderByTimestampDesc();
    }

    public AggregatedDataDTO aggregateData() {
        List<AparcamientoDTO> aparcamientos = getAverageBikesAvailable();
        aparcamientos = aparcamientos.stream().map(aparcamiento -> {
            AirQualityDTO airQuality = getClosestAirQuality(aparcamiento);
            aparcamiento.setAirQuality(airQuality);
            return aparcamiento;
        }).collect(Collectors.toList());

        AggregatedDataDTO aggregatedDataDTO = new AggregatedDataDTO();
        aggregatedDataDTO.setTimeStamp(Instant.now());
        aggregatedDataDTO.setAggregatedData(aparcamientos);
        saveAgregateDataDTO(aggregatedDataDTO);
        return aggregatedDataDTO;
    }

    private List<AparcamientoDTO> getAverageBikesAvailable() {
        List<AparcamientoDTO> aparcamientos = webClientBuilder.build()
                .get()
                .uri(bicisUrl + "/api/v1/average")
                .retrieve()
                .bodyToFlux(AparcamientoDTO.class)
                .collectList()
                .block();

        // Logging the retrieved data
        if (aparcamientos != null && !aparcamientos.isEmpty()) {
            aparcamientos.forEach(aparcamiento -> {
                System.out.println("Retrieved Aparcamiento: " + aparcamiento.getAparcamientoId() + ", Avg Bikes Available: " + aparcamiento.getAverageBikesAvailable());
            });
        } else {
            System.out.println("No aparcamientos retrieved or list is empty.");
        }

        return aparcamientos;
    }

    private AirQualityDTO getClosestAirQuality(AparcamientoDTO aparcamiento) {
        AirQualityDTO airQuality = webClientBuilder.build()
                .get()
                .uri(estacionesUrl + "/api/v1/average?lat=" + aparcamiento.getLatitud() + "&lon=" + aparcamiento.getLongitud())
                .retrieve()
                .bodyToMono(AirQualityDTO.class)
                .block();

        // Logging the retrieved data
        if (airQuality != null) {
            System.out.println("Retrieved Air Quality for Aparcamiento ID: " + aparcamiento.getAparcamientoId() +
                    ", Nitric Oxides: " + airQuality.getAvgNitricOxides() +
                    ", Nitrogen Dioxides: " + airQuality.getAvgNitrogenDioxides() +
                    ", VOCs_NMHC: " + airQuality.getAvgVOCs_NMHC() +
                    ", PM2.5: " + airQuality.getAvgPM2_5());
        } else {
            System.out.println("No air quality data retrieved for Aparcamiento ID: " + aparcamiento.getAparcamientoId());
        }

        return airQuality;
    }

    private void saveAgregateDataDTO(AggregatedDataDTO aggregatedDataDTO) {
        DTOrepo.save(aggregatedDataDTO);
    }
}
