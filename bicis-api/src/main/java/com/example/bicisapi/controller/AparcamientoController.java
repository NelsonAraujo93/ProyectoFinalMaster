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

@RestController
@RequestMapping("/api/v1/aparcamientos")
public class AparcamientoController {

    @Autowired
    private AparcamientoService aparcamientoService;

    @Autowired
    private AparcamientoStateService aparcamientoStateService;

    // Add a new parking spot (admin role)
    @PostMapping("")
    public Aparcamiento createAparcamiento(@RequestBody Aparcamiento aparcamiento) {
        try {
            Aparcamiento savedAparcamiento = aparcamientoService.save(aparcamiento);
            AparcamientoState aparcamientoState = new AparcamientoState();
            aparcamientoState.setAparcamientoId(savedAparcamiento.getId().toString());
            aparcamientoState.setOperation("open");
            aparcamientoState.setBikesAvailable(savedAparcamiento.getBikesCapacity());
            aparcamientoState.setFreeParkingSpots(0);
            aparcamientoStateService.createEvent(savedAparcamiento.getId().toString(), aparcamientoState);
            return savedAparcamiento;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create aparcamiento", e);
        }
    }

    // Delete a parking spot by id (admin role)
    @DeleteMapping("/{id}")
    public void deleteAparcamiento(@PathVariable Long id) {
        //verificar que exista antes de borrar
        try {
            aparcamientoService.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete aparcamiento", e);
        }
    }

    // Edit a parking spot (admin role)
    @PutMapping("/{id}")
    public Aparcamiento updateAparcamiento(@PathVariable Long id, @RequestBody Aparcamiento aparcamiento) {
        try {
            aparcamiento.setId(id);
            return aparcamientoService.save(aparcamiento);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update aparcamiento", e);
        }
    }

    // Get all parking spots (public endpoint)
    @GetMapping("")
    public List<Aparcamiento> getAllAparcamientos() {
        try {
            return aparcamientoService.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve aparcamientos", e);
        }
    }

    // Notify about a bike operation (parking role)
    @PostMapping("/evento/{id}")
    public AparcamientoState addAparcamientoState(@PathVariable String id, @RequestBody AparcamientoState aparcamientoState) {
        try {
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
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add aparcamiento state", e);
        }
    }

    // Get the current state of a parking spot or all state changes within a time range (public endpoint)
    @GetMapping("/{id}/status")
    public Optional<AparcamientoState> getAparcamientoStatus(@PathVariable String id,
                                                        @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                                        @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        try {
            if (from != null && to != null) {
                return aparcamientoStateService.findByAparcamientoIdAndTimestampBetween(id, from, to);
            } else {
                Optional<Aparcamiento> aparcamientoOptional = aparcamientoService.findById(id);
                if (aparcamientoOptional.isPresent()) {
                    return aparcamientoStateService.findTopByAparcamientoIdOrderByTimestampDesc(id);
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aparcamiento not found");
                }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve aparcamiento status", e);
        }
    }

    // Get the top 10 parking spots with the most available bikes at a given time (public endpoint)
    @GetMapping("/top10")
    public List<AparcamientoState> getTop10AparcamientosByBikesAvailable() {
        try {
            return aparcamientoStateService.findTop10ByBikesAvailable();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve top 10 aparcamientos", e);
        }
    }
}
