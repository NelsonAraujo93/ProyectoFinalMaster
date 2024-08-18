package com.example.apigateway.config;

import com.example.apigateway.filter.LoggingFilter;
import com.example.apigateway.security.AuthFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.web.cors.reactive.CorsWebFilter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class GatewayConfigTests {

    private LoggingFilter loggingFilter;
    private AuthFilter authFilter;
    private GatewayConfig gatewayConfig;

    @BeforeEach
    void setUp() {
        loggingFilter = mock(LoggingFilter.class);
        authFilter = mock(AuthFilter.class);
        gatewayConfig = new GatewayConfig(loggingFilter, authFilter);
    }

    @Test
    void testCustomRouteLocator() {
        RouteLocatorBuilder builder = mock(RouteLocatorBuilder.class);
        RouteLocatorBuilder.Builder routesBuilder = mock(RouteLocatorBuilder.Builder.class);
        RouteLocator routeLocator = mock(RouteLocator.class);

        when(builder.routes()).thenReturn(routesBuilder);
        when(routesBuilder.route(anyString(), any())).thenReturn(routesBuilder);
        when(routesBuilder.build()).thenReturn(routeLocator);

        RouteLocator result = gatewayConfig.customRouteLocator(builder);

        assertNotNull(result, "RouteLocator should not be null");
        verify(routesBuilder, times(5)).route(anyString(), any());
    }

    @Test
    void testCorsWebFilter() {
        // Test that the CorsWebFilter bean is created
        CorsWebFilter corsWebFilter = gatewayConfig.corsWebFilter();
        assertNotNull(corsWebFilter, "CorsWebFilter should not be null");
    }
}
