package com.example.bicisapi.dao;

public class AparcamientoFreeParkingSpotsDTO {
    private Long aparcamientoId;
    private double averageBikesAvailable;
    private double latitud;
    private double longitud;

    // Getters y Setters
    public Long getAparcamientoId() {
        return aparcamientoId;
    }

    public void setAparcamientoId(Long aparcamientoId) {
        this.aparcamientoId = aparcamientoId;
    }

    public double getAverageBikesAvailable() {
        return averageBikesAvailable;
    }

    public void setAverageBikesAvailable(double averageBikesAvailable) {
        this.averageBikesAvailable = averageBikesAvailable;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
