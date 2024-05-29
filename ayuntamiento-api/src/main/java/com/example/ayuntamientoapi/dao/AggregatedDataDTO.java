package com.example.ayuntamientoapi.dao;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "aggregated_data_dto")
public class AggregatedDataDTO {
    @Id
    private String id;
    private Instant timeStamp;
    private List<AparcamientoDTO> aggregatedData;

    public static class AparcamientoDTO {
        private int aparcamientoId;
        private double latitud;
        private double longitud;
        private double averageBikesAvailable;
        private AirQualityDTO airQuality;

        // Getters y Setters
        public int getAparcamientoId() {
            return aparcamientoId;
        }

        public void setAparcamientoId(int aparcamientoId) {
            this.aparcamientoId = aparcamientoId;
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

        public double getAverageBikesAvailable() {
            return averageBikesAvailable;
        }

        public void setAverageBikesAvailable(double averageBikesAvailable) {
            this.averageBikesAvailable = averageBikesAvailable;
        }

        public AirQualityDTO getAirQuality() {
            return airQuality;
        }

        public void setAirQuality(AirQualityDTO airQuality) {
            this.airQuality = airQuality;
        }
    }

    public static class AirQualityDTO {
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

    // Getters y Setters

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<AparcamientoDTO> getAggregatedData() {
        return aggregatedData;
    }

    public void setAggregatedData(List<AparcamientoDTO> aggregatedData) {
        this.aggregatedData = aggregatedData;
    }
}
