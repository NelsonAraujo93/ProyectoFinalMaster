package com.example.servicesapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "service_requests")
public class ServiceRequest {

    @Id
    private String id; // MongoDB ObjectId as a string
    private String serviceName; 
    private Long serviceId; // Reference to the ServiceModel ID stored in MySQL
    private Long clientId;
    private Date requestDate;
    private String status;
    private String details;

    // Default constructor
    public ServiceRequest() {
    }

    // Constructor
    public ServiceRequest(Long serviceId, Long clientId, Date requestDate, String status, String details) {
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
}
