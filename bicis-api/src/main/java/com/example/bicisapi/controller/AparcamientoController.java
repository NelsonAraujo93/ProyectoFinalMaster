package com.example.bicisapi.controller;

import com.example.bicisapi.domain.Aparcamiento;
import com.example.bicisapi.domain.AparcamientoState;
import com.example.bicisapi.exception.CustomException;
import com.example.bicisapi.service.AparcamientoService;
import com.example.bicisapi.service.AparcamientoStateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/aparcamiento")
public class AparcamientoController {

    @Autowired
    private AparcamientoService aparcamientoService;

    @Autowired
    private AparcamientoStateService aparcamientoStateService;

    // Add a new parking spot (admin role)
    @PostMapping("")
    public Aparcamiento createAparcamiento(@RequestBody Aparcamiento aparcamiento) {
        Aparcamiento savedAparcamiento = aparcamientoService.save(aparcamiento);
        AparcamientoState aparcamientoState = new AparcamientoState();
        aparcamientoState.setAparcamientoId(savedAparcamiento.getId().toString());
        aparcamientoState.setOperation("open");
        aparcamientoState.setBikesAvailable(savedAparcamiento.getBikesCapacity());
        aparcamientoState.setFreeParkingSpots(0);
        aparcamientoStateService.createEvent(savedAparcamiento.getId().toString(), aparcamientoState);
        return savedAparcamiento;
    }

    // Delete a parking spot by id (admin role)
    @DeleteMapping("/{id}")
    public void deleteAparcamiento(@PathVariable Long id) {
        aparcamientoService.deleteById(id);
    }

    // Edit a parking spot (admin role)
    @PutMapping("/{id}")
    public Aparcamiento updateAparcamiento(@PathVariable Long id, @RequestBody Aparcamiento aparcamiento) {
        aparcamiento.setId(id);
        return aparcamientoService.save(aparcamiento);
    }

    // Get all parking spots (public endpoint)
    @GetMapping("")
    public List<Aparcamiento> getAllAparcamientos() {
        return aparcamientoService.findAll();
    }

    // Notify about a bike operation (parking role)
    @PostMapping("/evento/{id}")
    public AparcamientoState addAparcamientoState(@PathVariable String id, @RequestBody AparcamientoState aparcamientoState) {
        Optional<Aparcamiento> aparcamientoOptional = aparcamientoService.findById(id);
        if (aparcamientoOptional.isPresent()) {
            aparcamientoState.setAparcamientoId(id);
            try {
                return aparcamientoStateService.createEvent(id, aparcamientoState);
            } catch (CustomException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aparcamiento not found");
        }
    }

    // Get the current state of a parking spot or all state changes within a time range (public endpoint)
    @GetMapping("/{id}/status")
    public List<AparcamientoState> getAparcamientoStatus(@PathVariable String id,
                                                        @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                                        @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
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
    @GetMapping("/top10")
    public List<AparcamientoState> getTop10AparcamientosByBikesAvailable() {
        return aparcamientoStateService.findTop10ByBikesAvailable();
    }
}
