package com.example.bicisapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Parking")
public class Aparcamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String direction;

    private int bikes_capacity;

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

    public int getBikesCapacity() {
        return this.bikes_capacity;
    }

    public void setBikesCapacity(int bikesCapacity) {
        this.bikes_capacity = bikesCapacity;
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