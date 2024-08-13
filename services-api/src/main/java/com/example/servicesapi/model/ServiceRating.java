package com.example.servicesapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "service_ratings")
public class ServiceRating {

    @Id
    private String id; // MongoDB ObjectId as a string

    private Long serviceId; // Reference to the ServiceModel ID stored in MySQL
    private Long clientId; // Reference to the client who rated the service
    private Double rating; // Rating given by the client
    private String comment; // Optional comment by the client
    private Date ratingDate; // Date when the rating was given

    // Default constructor
    public ServiceRating() {
    }

    // Constructor
    public ServiceRating(Long serviceId, Long clientId, Double rating, String comment, Date ratingDate) {
        this.serviceId = serviceId;
        this.clientId = clientId;
        this.rating = rating;
        this.comment = comment;
        this.ratingDate = ratingDate;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
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
