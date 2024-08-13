package com.example.servicesapi.dto;

import java.util.List;

public class PymeProfileDTO {
    private String pymeName;
    private String pymeDescription;
    private String pymePhone;
    private String pymePostalCode;
    private List<ServiceDTO> services;

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

    public List<ServiceDTO> getServices() {
        return services;
    }

    public void setServices(List<ServiceDTO> services) {
        this.services = services;
    }
}
