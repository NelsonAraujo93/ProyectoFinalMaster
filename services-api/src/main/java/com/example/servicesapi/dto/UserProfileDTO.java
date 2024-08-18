package com.example.servicesapi.dto;

import com.example.servicesapi.model.ServiceModel;
import com.example.servicesapi.model.ServiceRequest;
import com.example.servicesapi.model.Pyme;

import java.util.List;

public class UserProfileDTO {
  private Long userId;
  private String role;
  private List<ServiceModel> services;
  private List<ServiceRequest> requests;
  private Pyme pyme; // Add Pyme data

  // Getters and setters
  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public List<ServiceModel> getServices() {
    return services;
  }

  public void setServices(List<ServiceModel> services) {
    this.services = services;
  }

  public List<ServiceRequest> getRequests() {
    return requests;
  }

  public void setRequests(List<ServiceRequest> requests) {
    this.requests = requests;
  }

  public Pyme getPyme() {
    return pyme;
  }

  public void setPyme(Pyme pyme) {
    this.pyme = pyme;
  }
}
