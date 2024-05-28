package com.example.estacionesapi.controller;

import com.example.estacionesapi.domain.Estacion;
import com.example.estacionesapi.domain.EstacionLog;
import com.example.estacionesapi.exception.CustomException;
import com.example.estacionesapi.service.EstacionService;
import com.example.estacionesapi.service.EstacionLogService;

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
@RequestMapping("/api/v1/estaciones")
public class EstacionController {

    @Autowired
    private EstacionService es;

    @Autowired
    private EstacionLogService els;

    @PostMapping("")
    public Estacion createEstacion(@RequestBody Estacion estacion) {
        Estacion savedEstacion = es.save(estacion);
        EstacionLog estacionLog = new EstacionLog();
        estacionLog.setEstacionId(savedEstacion.getId().toString());
        estacionLog.setNitricOxides(0);
        estacionLog.setNitrogenDioxides(0);
        estacionLog.setPM2_5(0);
        estacionLog.setVOCs_NMHC(0);
        els.createLog(savedEstacion.getId().toString(), estacionLog);
        return savedEstacion;
    }

    @DeleteMapping("/{id}")
    public void deleteEstacion(@PathVariable Long id) {
        es.deleteById(id);
    }

    @PutMapping("/{id}")
    public Estacion updateEstacion(@PathVariable Long id, @RequestBody Estacion estacion) {
        estacion.setId(id);
        return es.save(estacion);
    }

    @GetMapping("")
    public List<Estacion> getAllEstaciones() {
        return es.findAll();
    }

    @PostMapping("/{id}")
    public EstacionLog addEstacionLog(@PathVariable String id, @RequestBody EstacionLog estacionLog) {
        Optional<Estacion> estacionOptional = es.findById(id);
        if (estacionOptional.isPresent()) {
            estacionLog.setEstacionId(id);
            try {
                return els.createLog(id, estacionLog);
            } catch (CustomException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estacion not found");
        }
    }

    @GetMapping("/{id}/status")
    public List<EstacionLog> getEstacionesStatus(@PathVariable String id,
                                                        @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                                        @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        try {
            Optional<Estacion> estacionOpcional = es.findById(id);
            if (!estacionOpcional.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aparcamiento not found");
            }

            if (from != null && to != null) {
                Logger logger = Logger.getLogger(EstacionController.class.getName());
                logger.info("from: " + from + " to: " + to);
                if (!from.isBefore(to)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The 'from' timestamp must be before the 'to' timestamp");
                }
                return els.findByEstacionIdAndTimestampBetween(id, from, to);
            } else {
                return els.findTopByEstacionIdOrderByTimestampDesc(id);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve aparcamiento status", e);
        }
    }
}
