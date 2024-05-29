package com.example.ayuntamientoapi.domain;

import java.time.Instant;


public class AggregatedData {

    private int aparcamientoId;
    private double avgBikesAvailable;
    private double avgNitricOxides;
    private double avgNitrogenDioxides;
    private double avgVOCs_NMHC;
    private double avgPM2_5;
    private Instant timeStamp;

    // Getters and Setters
    public int getAparcamientoId() {
        return aparcamientoId;
    }

    public void setAparcamientoId(int aparcamientoId) {
        this.aparcamientoId = aparcamientoId;
    }

    public double getAvgBikesAvailable() {
        return avgBikesAvailable;
    }

    public void setAvgBikesAvailable(double avgBikesAvailable) {
        this.avgBikesAvailable = avgBikesAvailable;
    }

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

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimestamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    // Getters y Setters
}
