package com.example.servicesapi.dto;

import java.util.Date;

public class ServiceRatingDTO {
    private String id; // Assuming MongoDB ObjectId is used
    private Long clientId;
    private Long serviceId;
    private Double rating;
    private String comment;
    private Date ratingDate; // Renamed for clarity

    // Default constructor
    public ServiceRatingDTO() {}

    // Constructor with fields
    public ServiceRatingDTO(String id, Long clientId, Long serviceId, Double rating, String comment, Date ratingDate) {
        this.id = id;
        this.clientId = clientId;
        this.serviceId = serviceId;
        this.rating = rating;
        this.comment = comment;
        this.ratingDate = ratingDate;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(Date ratingDate) {
        this.ratingDate = ratingDate;
    }
}
