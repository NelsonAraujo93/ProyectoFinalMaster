package com.example.estacionesapi.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

@Document(collection = "estaciones_logs")
public class EstacionLog {

    @Id
    private String id;
    private String estacionId;
    private Instant timestamp;
    private double nitricOxides;
    private double nitrogenDioxides;

    @JsonProperty("VOCs_NMHC")
    private double vocs_NMHC;

    @JsonProperty("PM2_5")
    private double pm2_5;

    public double getNitricOxides() {
      return nitricOxides;
    }

    public void setNitricOxides(double nitricOxides) {
      this.nitricOxides = nitricOxides;
    }

    public double getNitrogenDioxides() {
      return nitrogenDioxides;
    }

    public void setNitrogenDioxides(double nitrogenDioxides) {
      this.nitrogenDioxides = nitrogenDioxides;
    }

    public double getVOCs_NMHC() {
      return vocs_NMHC;
    }

    public void setVOCs_NMHC(double vocs_NMHC) {
      this.vocs_NMHC = vocs_NMHC;
    }

    public double getPM2_5() {
      return pm2_5;
    }

    public void setPM2_5(double PM2_5) {
      this.pm2_5 = PM2_5;
    }
    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstacionId() {
        return this.estacionId;
    }

    public void setEstacionId(String estacionId) {
        this.estacionId = estacionId;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
