package com.example.apigateway.config;

import com.example.apigateway.filter.LoggingFilter;
import com.example.apigateway.security.AuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class GatewayConfig {

  private final LoggingFilter loggingFilter;
  private final AuthFilter authFilter;

  public GatewayConfig(LoggingFilter loggingFilter, AuthFilter authFilter) {
    this.loggingFilter = loggingFilter;
    this.authFilter = authFilter;
  }

  @Value("${api.security.url}")
  private String seguridadUrl;

  @Value("${api.users.url}")
  private String usersUrl;

  @Value("${api.services.url}")
  private String servicesUrl;

  @Bean
  RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
        .route("auth_route", r -> r.path("/auth/**")
            .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config())))
            .uri(seguridadUrl))
        .route("user_management", r -> r.path("/users/**")
            .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config()))
                .filter(authFilter.apply(new AuthFilter.Config(Arrays.asList("PYME", "CLIENT")))))
            .uri(usersUrl))
        .route("service_management", r -> r.path("/services/**")
            .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config()))
                .filter(authFilter.apply(new AuthFilter.Config(Arrays.asList("PYME")))))
            .uri(servicesUrl))
        .route("service_requests_management", r -> r.path("/requests/**")
            .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config()))
                .filter(authFilter.apply(new AuthFilter.Config(Arrays.asList("PYME", "CLIENT")))))
            .uri(servicesUrl))
        .build();
  }

  @Bean
  public CorsWebFilter corsWebFilter() {
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.setAllowCredentials(true);
    corsConfig.addAllowedOriginPattern("*");
    corsConfig.addAllowedHeader("*");
    corsConfig.addAllowedMethod("*");

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfig);

    return new CorsWebFilter(source);
  }
}
