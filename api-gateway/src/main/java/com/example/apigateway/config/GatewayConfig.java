package com.example.apigateway.config;

import com.example.apigateway.filter.LoggingFilter;
import com.example.apigateway.security.AuthFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final LoggingFilter loggingFilter;
    private final AuthFilter authFilter;

    public GatewayConfig(LoggingFilter loggingFilter, AuthFilter authFilter) {
        this.loggingFilter = loggingFilter;
        this.authFilter = authFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth_route", r -> r.path("/auth/**")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config())))
                        .uri("http://localhost:8083"))
                .route("bicis_route", r -> r.path("/api/v1/aparcamiento/**")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config()))
                                .filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("http://localhost:8081"))
                .route("estaciones_route", r -> r.path("/api/v1/estaciones/**")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config()))
                                .filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("http://localhost:8082"))
                .build();
    }
}
