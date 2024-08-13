package com.example.servicesapi.dto;

public class ServiceDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long pymeId;

    // Getters and setters
    // ...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getPymeId() {
        return pymeId;
    }

    public void setPymeId(Long pymeId) {
        this.pymeId = pymeId;
    }
}
