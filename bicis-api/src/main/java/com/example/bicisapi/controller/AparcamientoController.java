package com.example.bicisapi.controller;

import com.example.bicisapi.dao.AparcamientoFreeParkingSpotsDTO;
import com.example.bicisapi.dao.AparcamientoStateDTO;
import com.example.bicisapi.domain.Aparcamiento;
import com.example.bicisapi.domain.AparcamientoState;
import com.example.bicisapi.exception.CustomException;
import com.example.bicisapi.service.AparcamientoService;
import com.example.bicisapi.service.AparcamientoStateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1")
public class AparcamientoController {

    @Autowired
    private AparcamientoService aparcamientoService;

    @Autowired
    private AparcamientoStateService aparcamientoStateService;

    private final Logger logger = Logger.getLogger(AparcamientoController.class.getName());

    // Add a new parking spot (admin role)
    @PostMapping("/aparcamiento")
    public Aparcamiento createAparcamiento(@RequestBody Aparcamiento aparcamiento) {
        if (aparcamiento == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aparcamiento data is required");
        }
        Aparcamiento savedAparcamiento = aparcamientoService.save(aparcamiento);
        return savedAparcamiento;
    }

    // Delete a parking spot by id (admin role)
    @DeleteMapping("/aparcamiento/{id}")
    public void deleteAparcamiento(@PathVariable String id) {
        if (id == null || id.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aparcamiento id is required");
        }
        Long longId = Long.parseLong(id);
        Optional<Aparcamiento> aparcamientoOptional = aparcamientoService.findById(id);
        if (aparcamientoOptional.isPresent()) {
            aparcamientoService.deleteById(longId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aparcamiento not found");
        }
    }

    // Edit a parking spot (admin role)
    @PutMapping("/aparcamiento/{id}")
    public Aparcamiento updateAparcamiento(@PathVariable Long id, @RequestBody Aparcamiento aparcamiento) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aparcamiento id is required");
        }
        if (aparcamiento == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aparcamiento data is required");
        }
        Optional<Aparcamiento> aparcamientoOptional = aparcamientoService.findById(id.toString());
        if (!aparcamientoOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aparcamiento not found");
        }
        aparcamiento.setId(id);
        return aparcamientoService.save(aparcamiento);
    }

    // Get all parking spots (public endpoint)
    @GetMapping("/aparcamientos")
    public List<Aparcamiento> getAllAparcamientos() {
        return aparcamientoService.findAll();
    }

    @PostMapping("/evento/{id}")
    public AparcamientoState addAparcamientoState(@PathVariable String id, @RequestBody AparcamientoState aparcamientoState) {
        Optional<Aparcamiento> aparcamientoOptional = aparcamientoService.findById(id);
        if (aparcamientoOptional.isPresent()) {
            aparcamientoState.setAparcamientoId(id);
            try {
                return aparcamientoStateService.createEvent(id, aparcamientoState);
            } catch (CustomException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aparcamiento not found");
        }
    }

    // Get the current state of a parking spot or all state changes within a time range (public endpoint)
    @GetMapping("/aparcamiento/{id}/status")
    public List<AparcamientoState> getAparcamientoStatus(@PathVariable String id,
                                                        @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                                        @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        if (id == null || id.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aparcamiento id is required");
        }
        try {
            Optional<Aparcamiento> aparcamientoOptional = aparcamientoService.findById(id);
            if (!aparcamientoOptional.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aparcamiento not found");
            }

            if (from != null && to != null) {
                Logger logger = Logger.getLogger(AparcamientoController.class.getName());
                logger.info("from: " + from + " to: " + to);
                if (!from.isBefore(to)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The 'from' timestamp must be before the 'to' timestamp");
                }
                return aparcamientoStateService.findByAparcamientoIdAndTimestampBetween(id, from, to);
            } else {
                return aparcamientoStateService.findTopByAparcamientoIdOrderByTimestampDesc(id);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve aparcamiento status", e);
        }
    }

    // Get the top 10 parking spots with the most available bikes at a given time (public endpoint)
    @GetMapping("/aparcamientos/top10")
    public List<AparcamientoState> getTop10AparcamientosByBikesAvailable() {
        return aparcamientoStateService.findTop10ByBikesAvailable();
    }

    @GetMapping("/aparcamientoCercano")
    public ResponseEntity<AparcamientoStateDTO> findNearestAparcamientoWithBikes(@RequestParam double lat, @RequestParam double lon) {
        logger.info("Received request to find nearest aparcamiento with bikes for lat: " + lat + ", lon: " + lon);
        try {
            AparcamientoStateDTO aparcamiento = aparcamientoService.findNearestAparcamientoWithBikes(lat, lon);
            logger.info("Nearest aparcamiento found: " + aparcamiento);
            return ResponseEntity.ok(aparcamiento);
        } catch (RuntimeException e) {
            logger.severe("No available bikes found in nearest aparcamientos");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/average")
    public ResponseEntity<List<AparcamientoFreeParkingSpotsDTO>> getAverageFreeParkingSpots() {
        List<AparcamientoFreeParkingSpotsDTO> averages = aparcamientoService.calculateAverageFreeParkingSpots();
        return ResponseEntity.ok(averages);
    }
}
