package com.example.estacionesapi.dao;

public class AirQualityDTO {
    private double avgNitricOxides;
    private double avgNitrogenDioxides;
    private double avgVOCs_NMHC;
    private double avgPM2_5;

    // Getters y Setters

    public double getAvgNitricOxides() {
        return avgNitricOxides;
    }

    public void setAvgNitricOxides(double avgNitricOxides) {
        this.avgNitricOxides = avgNitricOxides;
    }

    public double getAvgNitrogenDioxides() {
        return avgNitrogenDioxides;
    }

    public void setAvgNitrogenDioxides(double avgNitrogenDioxides) {
        this.avgNitrogenDioxides = avgNitrogenDioxides;
    }

    public double getAvgVOCs_NMHC() {
        return avgVOCs_NMHC;
    }

    public void setAvgVOCs_NMHC(double avgVOCs_NMHC) {
        this.avgVOCs_NMHC = avgVOCs_NMHC;
    }

    public double getAvgPM2_5() {
        return avgPM2_5;
    }

    public void setAvgPM2_5(double avgPM2_5) {
        this.avgPM2_5 = avgPM2_5;
    }
}
