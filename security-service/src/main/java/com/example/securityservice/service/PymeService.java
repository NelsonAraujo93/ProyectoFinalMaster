package com.example.securityservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.securityservice.model.Pyme;
import com.example.securityservice.repository.PymeRepository;

@Service
public class PymeService {

    @Autowired
    private PymeRepository pymeRepository;

    public Pyme getPymeByUserId(Long userId) {
        return pymeRepository.findByUserId(userId).orElse(null);
    }
}