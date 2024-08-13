package com.example.servicesapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "service")
public class ServiceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @ManyToOne
    @JoinColumn(name = "pyme_id", nullable = false)
    private Pyme pyme;

    // Default constructor
    public ServiceModel() {
    }

    // Constructor
    public ServiceModel(String name, String description, double price, Pyme pyme) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.pyme = pyme;
    }

    // Getters and setters
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

    public Pyme getPyme() {
        return pyme;
    }

    public void setPyme(Pyme pyme) {
        this.pyme = pyme;
    }
}
