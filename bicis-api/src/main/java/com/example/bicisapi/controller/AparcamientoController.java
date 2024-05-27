package com.example.bicisapi.controller;

import com.example.bicisapi.domain.Aparcamiento;
import com.example.bicisapi.domain.AparcamientoState;
import com.example.bicisapi.service.AparcamientoService;
import com.example.bicisapi.service.AparcamientoStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

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
        return aparcamientoService.save(aparcamiento);
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
        aparcamientoState.setId(id);
        return aparcamientoStateService.save(aparcamientoState);
    }

    // Get the current state of a parking spot or all state changes within a time range (public endpoint)
    @GetMapping("/{id}/status")
    public List<AparcamientoState> getAparcamientoStatus(@PathVariable String id,
                                                        @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                                        @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        if (from != null && to != null) {
            return aparcamientoStateService.findByAparcamientoIdAndTimestampBetween(id, from, to);
        } else {
            return aparcamientoStateService.findByAparcamientoId(id);
        }
    }

    // Get the top 10 parking spots with the most available bikes at a given time (public endpoint)
    @GetMapping("/top")
    public List<AparcamientoState> getTopAparcamientos(@RequestParam("timestamp") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant timestamp) {
        return aparcamientoStateService.findTop10ByTimeStamp();
    }
}
