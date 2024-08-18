package com.example.servicesapi.dto;

import com.example.servicesapi.model.ServiceRequest;

import java.util.List;

public class ServiceDetailsDTO {
  private ServiceDTO service;
  private List<ServiceRequest> requests;

  // Getters and setters
  public ServiceDTO getService() {
    return service;
  }

  public void setService(ServiceDTO service) {
    this.service = service;
  }

  public List<ServiceRequest> getRequests() {
    return requests;
  }

  public void setRequests(List<ServiceRequest> requests) {
    this.requests = requests;
  }
}
