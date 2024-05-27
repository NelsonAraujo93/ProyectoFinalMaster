package com.example.bicisapi.controller;

import com.example.bicisapi.domain.Aparcamiento;
import com.example.bicisapi.service.AparcamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/bicisapi")
public class AparcamientoController {

    @Autowired
    private AparcamientoService aparcamientoService;

    @GetMapping("/aparcamientos")
    public List<Aparcamiento> getAllAparcamientos() {
        return aparcamientoService.findAll();
    }

    @PostMapping("/aparcamientos")
    public Aparcamiento createAparcamiento(@RequestBody Aparcamiento aparcamiento) {
        return aparcamientoService.save(aparcamiento);
    }
}
