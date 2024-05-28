package com.example.bicisapi.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "aparcamiento_states")
public class AparcamientoState {

    @Id
    private String id;
    private String aparcamientoId;
    private String operation;
    private int bikesAvailable;
    private int freeParkingSpots;
    private Instant timestamp;
    private int amountOfBikes;

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAparcamientoId() {
        return this.aparcamientoId;
    }

    public void setAparcamientoId(String aparcamientoId) {
        this.aparcamientoId = aparcamientoId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getBikesAvailable() {
        return this.bikesAvailable;
    }

    public void setBikesAvailable(int bikesAvailable) {
        this.bikesAvailable = bikesAvailable;
    }

    public int getFreeParkingSpots() {
        return this.freeParkingSpots;
    }

    public void setFreeParkingSpots(int freeParkingSpots) {
        this.freeParkingSpots = freeParkingSpots;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getAmountOfBikes() {
        return this.amountOfBikes;
    }

    public void setAmountOfBikes(int amountOfBikes) {
        this.amountOfBikes = amountOfBikes;
    }
}
