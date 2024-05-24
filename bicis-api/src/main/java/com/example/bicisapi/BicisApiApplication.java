package com.example.bicisapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BicisApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(BicisApiApplication.class, args);
    }
}
