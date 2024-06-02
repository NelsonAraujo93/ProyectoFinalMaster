package com.example.estacionesapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "estacion")
public class Estacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String direction;

    private double latitude;

    private double longitude;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDirection() {
        return this.direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getLatitud() {
        return this.latitude;
    }

    public void setLatitud(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitud() {
        return this.longitude;
    }

    public void setLongitud(double longitude) {
        this.longitude = longitude;
    }
}