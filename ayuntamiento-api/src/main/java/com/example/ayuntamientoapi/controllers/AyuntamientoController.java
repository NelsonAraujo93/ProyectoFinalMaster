package com.example.ayuntamientoapi.controlador;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    public ControladorAyuntamiento(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/bicis")
    public ResponseEntity<String> obtenerBicis() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        return restTemplate.getForEntity(bicisApiUrl + "/api/aparcamientos", String.class, headers);
    }

    @GetMapping("/estaciones")
    public ResponseEntity<String> obtenerEstaciones() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        return restTemplate.getForEntity(estacionesApiUrl + "/api/estaciones", String.class, headers);
    }
}
