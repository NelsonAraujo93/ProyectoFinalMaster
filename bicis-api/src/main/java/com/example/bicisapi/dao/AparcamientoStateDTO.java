package com.example.bicisapi.dao;

public class AparcamientoStateDTO {
    private Long id;
    private String direction;
    private int bikesCapacity;
    private double latitude;
    private double longitude;
    private int currentBikeAvailability;
    private int freeParkingSpots;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getBikesCapacity() {
        return bikesCapacity;
    }

    public void setBikesCapacity(int bikesCapacity) {
        this.bikesCapacity = bikesCapacity;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getCurrentBikeAvailability() {
        return currentBikeAvailability;
    }

    public void setCurrentBikeAvailability(int currentBikeAvailability) {
        this.currentBikeAvailability = currentBikeAvailability;
    }

    public int getFreeParkingSpots() {
        return freeParkingSpots;
    }

    public void setFreeParkingSpots(int freeParkingSpots) {
        this.freeParkingSpots = freeParkingSpots;
    }
}
