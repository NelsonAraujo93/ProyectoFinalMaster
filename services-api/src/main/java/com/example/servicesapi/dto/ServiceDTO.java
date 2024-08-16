package com.example.servicesapi.dto;

public class ServiceDTO {

    private Integer id;
    private String name;
    private String description;
    private double price;
    private double averageRating;
    private int ratingCount;

     
    // Pyme details
    private Integer pymeId;
    private String pymeName;
    private String pymeDescription;
    private String pymePhone;
    private String pymePostalCode;

    // Getters and setters

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

    public Integer getPymeId() {
        return pymeId;
    }

    public void setPymeId(Integer pymeId) {
        this.pymeId = pymeId;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getPymeName() {
        return pymeName;
    }

    public void setPymeName(String pymeName) {
        this.pymeName = pymeName;
    }

    public String getPymeDescription() {
        return pymeDescription;
    }

    public void setPymeDescription(String pymeDescription) {
        this.pymeDescription = pymeDescription;
    }

    public String getPymePhone() {
        return pymePhone;
    }

    public void setPymePhone(String pymePhone) {
        this.pymePhone = pymePhone;
    }

    public String getPymePostalCode() {
        return pymePostalCode;
    }

    public void setPymePostalCode(String pymePostalCode) {
        this.pymePostalCode = pymePostalCode;
    }
}
