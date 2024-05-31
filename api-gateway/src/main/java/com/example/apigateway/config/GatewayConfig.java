package com.example.apigateway.config;

import com.example.apigateway.filter.LoggingFilter;
import com.example.apigateway.security.AuthFilter;

import org.springframework.beans.factory.annotation.Value;
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

        @Value("${api.bicis.url}")
        private String bicisUrl;

        @Value("${api.seguridad.url}")
        private String seguridadUrl;

        @Value("${api.estaciones.url}")
        private String estacionesUrl;

        @Value("${api.ayuntamiento.url}")
        private String ayuntamientoUrl;

        @Bean
        RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth_route", r -> r.path("/auth/**")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config())))
                        .uri(seguridadUrl))
                .route("admin_estacion", r -> r.path("/api/v1/estacion/**")
                        .and()
                        .method("POST", "PUT", "DELETE")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config()))
                                .filter(authFilter.apply(new AuthFilter.Config("ADMIN"))))
                        .uri(ayuntamientoUrl))
                .route("admin_aparcamiento", r -> r.path("/api/v1/aparcamiento/**")
                        .and()
                        .method("POST", "PUT", "DELETE")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config()))
                                .filter(authFilter.apply(new AuthFilter.Config("ADMIN"))))
                        .uri(ayuntamientoUrl))
                .route("aggregate_data_service", r -> r.path("/api/v1/aggregateData/**")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config()))
                                .filter(authFilter.apply(new AuthFilter.Config("SERVICIO"))))
                        .uri(ayuntamientoUrl)) // Assuming AyuntamientoAPI is running on 8084
                .route("estacion_estacion", r -> r.path("/api/v1/log/{id}")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config()))
                                .filter(authFilter.apply(new AuthFilter.Config("ESTACION"))))
                        .uri(estacionesUrl))
                .route("aparcamiento_aparcamiento", r -> r.path("/api/v1/evento/{id}")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config()))
                                .filter(authFilter.apply(new AuthFilter.Config("APARCAMIENTO"))))
                        .uri(bicisUrl))
                .route("public_aparcamientos", r -> r.path("/api/v1/aparcamientos")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config())))
                        .uri(bicisUrl))
                .route("public_aparcamiento_status", r -> r.path("/api/v1/aparcamiento/{id}/status")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config())))
                        .uri(bicisUrl))
                .route("public_aggregated_data", r -> r.path("/api/v1/aparcamientos/top10")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config())))
                        .uri(bicisUrl))
                .route("public_estaciones", r -> r.path("/api/v1/estaciones")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config())))
                        .uri(estacionesUrl))
                .route("public_estacion_status", r -> r.path("/api/v1/estacion/{id}/status")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config())))
                        .uri(estacionesUrl))
                .route("public_aparcamiento_cercano", r -> r.path("/api/v1/aparcamientoCercano")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config())))
                        .uri(ayuntamientoUrl)) // Assuming AyuntamientoAPI is running on 8084
                .route("public_aggregated_data", r -> r.path("/api/v1/aggregatedData")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config())))
                        .uri(ayuntamientoUrl)) // Assuming AyuntamientoAPI is running on 8084
                .build();
    }
}
