package com.example.servicesapi.dto;

import java.util.List;

import com.example.servicesapi.model.ServiceModel;

public class PymeDTO {
    private Long id;
    private String pymePostalCode;
    private String pymePhone;
    private String pymeName;
    private String pymeDescription;
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

    public List<ServiceModel> getServices() {
        return services;
    }

    public void setServices(List<ServiceModel> services) {
        this.services = services;
    }
}
