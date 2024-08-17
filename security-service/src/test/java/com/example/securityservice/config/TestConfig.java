package com.example.securityservice.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.web.reactive.server.WebTestClient;

@TestConfiguration
@EnableJpaRepositories(basePackages = "com.example.securityservice.repository")
public class TestConfig {
    // Additional test-specific beans or configurations
    @Bean
    public WebTestClient webTestClient() {
        return WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080") // Adjust as necessary
                .build();
    }
}
