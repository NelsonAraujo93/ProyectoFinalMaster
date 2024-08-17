package com.example.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ApiGateWayApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        // This test will pass if the application context loads successfully
    }

    @Test
    void mainMethodTest() {
        ApiGatewayApplication.main(new String[] {});
    }

    @Test
    void webClientBuilderBeanIsCreated() {
        // This test ensures that the WebClient.Builder bean is present in the application context
        WebClient.Builder webClientBuilder = context.getBean(WebClient.Builder.class);
        assertNotNull(webClientBuilder, "WebClient.Builder bean should be created");
    }
}
