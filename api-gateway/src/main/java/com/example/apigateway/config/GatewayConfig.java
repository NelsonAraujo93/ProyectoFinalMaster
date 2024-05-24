package com.example.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("bicis_api", r -> r.path("/api/bicis/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://bicis-api"))
                .route("estaciones_api", r -> r.path("/api/estaciones/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://estaciones-api"))
                .route("ayuntamiento_api", r -> r.path("/api/ayuntamiento/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://ayuntamiento-api"))
                .build();
    }
}
