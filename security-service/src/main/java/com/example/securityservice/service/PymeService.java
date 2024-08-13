package com.example.securityservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.securityservice.dto.PymeDTO;
import com.example.securityservice.model.Pyme;
import com.example.securityservice.model.User;
import com.example.securityservice.repository.PymeRepository;
import com.example.securityservice.repository.UserRepository;

@Service
public class PymeService {

    @Autowired
    private PymeRepository pymeRepository;

    public Pyme getPymeByUserId(Long userId) {
        return pymeRepository.findByUserId(userId).orElse(null);
    }
}