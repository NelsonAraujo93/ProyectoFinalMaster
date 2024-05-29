package com.example.estacionesapi.dao;

public class EstacionContaminantesPromedioDTO {
    private Long estacionId;
    private double latitude;
    private double longitude;
    private double averageNitricOxides;
    private double averageNitrogenDioxides;
    private double averageVOCs_NMHC;
    private double averagePM2_5;

    // Getters y Setters

    public Long getEstacionId() {
        return estacionId;
    }

    public void setEstacionId(Long estacionId) {
        this.estacionId = estacionId;
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

    public double getAverageNitricOxides() {
        return averageNitricOxides;
    }

    public void setAverageNitricOxides(double averageNitricOxides) {
        this.averageNitricOxides = averageNitricOxides;
    }

    public double getAverageNitrogenDioxides() {
        return averageNitrogenDioxides;
    }

    public void setAverageNitrogenDioxides(double averageNitrogenDioxides) {
        this.averageNitrogenDioxides = averageNitrogenDioxides;
    }

    public double getAverageVOCs_NMHC() {
        return averageVOCs_NMHC;
    }

    public void setAverageVOCs_NMHC(double averageVOCs_NMHC) {
        this.averageVOCs_NMHC = averageVOCs_NMHC;
    }

    public double getAveragePM2_5() {
        return averagePM2_5;
    }

    public void setAveragePM2_5(double averagePM2_5) {
        this.averagePM2_5 = averagePM2_5;
    }
}
