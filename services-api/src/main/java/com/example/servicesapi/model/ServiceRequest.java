package com.example.servicesapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "service_requests")
public class ServiceRequest {

    @Id
    private String id; // MongoDB ObjectId as a string
    private String serviceName; 
    private Integer serviceId; // Reference to the ServiceModel ID stored in MySQL
    private Integer clientId;
    private Date requestDate;
    private String status;
    private String details;
    // New fields for finalization
    private Double rating;
    private String comment;
    private Date ratingDate;

    // Default constructor
    public ServiceRequest() {
    }

    // Constructor
    public ServiceRequest(String serviceName, Integer serviceId, Integer clientId, Date requestDate, String status, String details) {
        this.serviceName = serviceName;
        this.serviceId = serviceId;
        this.clientId = clientId;
        this.requestDate = requestDate;
        this.status = status;
        this.details = details;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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
