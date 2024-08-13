package com.example.servicesapi.dto;

public class FinalizeRequestDTO {
    private Double rating;
    private String comment;

    // Getters and Setters
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
}
