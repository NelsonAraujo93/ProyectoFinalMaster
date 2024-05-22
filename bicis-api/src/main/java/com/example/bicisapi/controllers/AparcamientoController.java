package com.example.bicisapi.controllers;

import com.example.bicisapi.domain.Aparcamiento;
import com.example.bicisapi.services.AparcamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/aparcamiento")

public class AparcamientoController {

    @Autowired
    private AparcamientoService servicioAparcamiento;

    @PostMapping
    public Aparcamiento agregarAparcamiento(@RequestBody Aparcamiento aparcamiento) {
        return servicioAparcamiento.agregarAparcamiento(aparcamiento);
    }

    @DeleteMapping("/{id}")
    public void eliminarAparcamiento(@PathVariable Long id) {
        servicioAparcamiento.eliminarAparcamiento(id);
    }

    @PutMapping
    public Aparcamiento editarAparcamiento(@RequestBody Aparcamiento aparcamiento) {
        return servicioAparcamiento.editarAparcamiento(aparcamiento);
    }

    @GetMapping
    public List<Aparcamiento> obtenerTodosAparcamientos() {
        return servicioAparcamiento.obtenerTodosAparcamientos();
    }
}
