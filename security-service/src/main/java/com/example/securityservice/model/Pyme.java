package com.example.securityservice.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "pyme")
public class Pyme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pyme_postal_code", nullable = false)
    private String pymePostalCode;

    @Column(name = "pyme_phone", nullable = false)
    private String pymePhone;

    @Column(name = "pyme_name", nullable = false)
    private String pymeName;

    @Column(name = "pyme_description")
    private String pymeDescription;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "pyme", cascade = CascadeType.ALL, orphanRemoval = true) // Update to reference pyme
    private List<ServiceModel> services;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPymePostalCode() {
        return pymePostalCode;
    }

    public void setPymePostalCode(String pymePostalCode) {
        this.pymePostalCode = pymePostalCode;
    }

    public String getPymePhone() {
        return pymePhone;
    }

    public void setPymePhone(String pymePhone) {
        this.pymePhone = pymePhone;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ServiceModel> getServices() {
        return services;
    }

    public void setServices(List<ServiceModel> services) {
        this.services = services;
    }
}