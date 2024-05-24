package com.example.ayuntamientoapi.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/ayuntamiento")
public class AyuntamientoController {

    @Value("${bicis.api.url}")
    private String bicisApiUrl;

    @Value("${estaciones.api.url}")
    private String estacionesApiUrl;

    @Value("${api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public AyuntamientoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/bicis")
    public ResponseEntity<String> obtenerBicis(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token);
        headers.set("Api-Key", apiKey);
        return restTemplate.exchange(bicisApiUrl + "/api/aparcamientos", HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    @GetMapping("/estaciones")
    public ResponseEntity<String> obtenerEstaciones(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token);
        headers.set("Api-Key", apiKey);
        return restTemplate.exchange(estacionesApiUrl + "/api/estaciones", HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }
}
