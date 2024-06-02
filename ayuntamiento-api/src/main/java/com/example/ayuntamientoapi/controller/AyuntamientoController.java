package com.example.ayuntamientoapi.controller;

import com.example.ayuntamientoapi.dao.AggregatedDataDTO;
import com.example.ayuntamientoapi.service.AggregateDataDTOService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@RestController
@RequestMapping("/api/v1")
public class AyuntamientoController {

    @Autowired
    private AggregateDataDTOService add;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${api.bicis.url}")
    private String bicisUrl;

    @Value("${api.estaciones.url}")
    private String estacionesUrl;

    @GetMapping("/aggregatedData")
    public ResponseEntity<AggregatedDataDTO> getLastAggregatedDataDTO() {
        return add.getLastAggregatedData()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/aggregateData")
    public ResponseEntity<AggregatedDataDTO> aggregateData() {
        AggregatedDataDTO aggregatedData = add.aggregateData();
        return ResponseEntity.ok(aggregatedData);
    }

    @GetMapping("/aparcamientoCercano")
    public ResponseEntity<String> findNearestAparcamientoWithBikes(@RequestParam double lat, @RequestParam double lon) {
        try {
            String response = webClientBuilder.build()
                    .get()
                    .uri(bicisUrl + "/api/v1/aparcamientoCercano?lat={lat}&lon={lon}", lat, lon)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return ResponseEntity.ok(response);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aparcamiento not found");
            } else if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
            } else {
                return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
            }
        }
    }

    @PostMapping("/estacion")
    public ResponseEntity<String> createEstacion(@RequestBody String estacion) {
        return webClientBuilder.build()
                .post()
                .uri(estacionesUrl + "/api/v1/estacion")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(estacion)
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    @PutMapping("/estacion/{id}")
    public ResponseEntity<String> updateEstacion(@PathVariable Long id, @RequestBody String estacion) {
        try {
            return webClientBuilder.build()
                    .put()
                    .uri(estacionesUrl + "/api/v1/estacion/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(estacion)
                    .retrieve()
                    .toEntity(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estacion not found");
            } else {
                throw e;
            }
        }
    }

    @DeleteMapping("/estacion/{id}")
    public ResponseEntity<String> deleteEstacion(@PathVariable Long id) {
        try {
            return webClientBuilder.build()
                    .delete()
                    .uri(estacionesUrl + "/api/v1/estacion/" + id)
                    .retrieve()
                    .toEntity(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estacion not found");
            } else {
                throw e;
            }
        }
    }

    @PostMapping("/aparcamiento")
    public ResponseEntity<String> createAparcamiento(@RequestBody String aparcamiento) {
        return webClientBuilder.build()
                .post()
                .uri(bicisUrl + "/api/v1/aparcamiento")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(aparcamiento)
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    @PutMapping("/aparcamiento/{id}")
    public ResponseEntity<String> updateAparcamiento(@PathVariable Long id, @RequestBody String aparcamiento) {
        try {
            return webClientBuilder.build()
                    .put()
                    .uri(bicisUrl + "/api/v1/aparcamiento/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(aparcamiento)
                    .retrieve()
                    .toEntity(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aparcamiento not found");
            } else {
                throw e;
            }
        }
    }

    @DeleteMapping("/aparcamiento/{id}")
    public ResponseEntity<String> deleteAparcamiento(@PathVariable Long id) {
        try {
            return webClientBuilder.build()
                    .delete()
                    .uri(bicisUrl + "/api/v1/aparcamiento/" + id)
                    .retrieve()
                    .toEntity(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aparcamiento not found");
            } else {
                throw e;
            }
        }
    }
}
