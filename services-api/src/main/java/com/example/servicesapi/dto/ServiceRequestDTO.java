package com.example.servicesapi.dto;

public class ServiceRequestDTO {
  private Integer id;
  private String name;
  private String description;
  private double price;
  private double averageRating;

  public ServiceRequestDTO() {
  }
  // Constructor with fields
  public ServiceRequestDTO(Integer id, String name, String description, double price, double averageRating) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.averageRating = averageRating;
  }

  // Getters and Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public double getAverageRating() {
    return averageRating;
  }

  public void setAverageRating(double averageRating) {
    this.averageRating = averageRating;
  }
}
