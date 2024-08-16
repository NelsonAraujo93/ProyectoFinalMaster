package com.example.servicesapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "services")
public class ServiceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // Change from Long to Integer

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "total_rating", nullable = false)
    private double totalRating = 0.0;

    @Column(name = "rating_count", nullable = false)
    private int ratingCount = 0;

    @Column(name = "average_rating", nullable = false)
    private double averageRating = 0.0;

    @Column(name = "pyme_id", nullable = false)
    private Integer pymeId;  // Reference to Pyme by ID instead of using the entity

    // Default constructor
    public ServiceModel() {
    }

    // Constructor
    public ServiceModel(String name, String description, double price, Integer pymeId) {    
        this.name = name;
        this.description = description;
        this.price = price;
        this.pymeId = pymeId;
    }

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

    public double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(double totalRating) {
        this.totalRating = totalRating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
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

    // Method to add a rating to the service
    public void addRating(double rating) {
        totalRating += rating;
        ratingCount++;
        averageRating = totalRating / ratingCount;
    }

    // Method to update a rating for the service
    public void updateRating(double oldRating, double newRating) {
        totalRating = totalRating - oldRating + newRating;
        averageRating = totalRating / ratingCount;
    }
}
