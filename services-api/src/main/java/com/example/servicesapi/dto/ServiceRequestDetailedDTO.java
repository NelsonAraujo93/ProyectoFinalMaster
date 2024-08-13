package com.example.servicesapi.dto;

import java.util.Date;
import java.util.List;

public class ServiceRequestDetailedDTO {
   private Long serviceId;
    private String serviceName;
    private String serviceDescription;
    private Double servicePrice;
    private Double averageRating;
    private List<ServiceRatingDTO> ratings;
    private Date date;


    // Default constructor
    public ServiceRequestDetailedDTO() {}

    // Constructor with fields
    public ServiceRequestDetailedDTO(Long serviceId, String serviceName, String serviceDescription, Double servicePrice, Double averageRating, List<ServiceRatingDTO> ratings) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.servicePrice = servicePrice;
        this.averageRating = averageRating;
        this.ratings = ratings;
    }

    // Getters and Setters
    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public Double getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(Double servicePrice) {
        this.servicePrice = servicePrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public List<ServiceRatingDTO> getRatings() {
        return ratings;
    }

    public void setRatings(List<ServiceRatingDTO> ratings) {
        this.ratings = ratings;
    }
}
