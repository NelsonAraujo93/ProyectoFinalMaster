package com.example.apigateway.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.mockito.Mockito.*;

class LoggingFilterTests {

    private LoggingFilter loggingFilter;
    private Logger logger;

    @BeforeEach
    void setUp() {
        loggingFilter = new LoggingFilter();
        logger = LoggerFactory.getLogger(LoggingFilter.class);
    }

    @Test
    void testLoggingFilter() {
        // Mocking the ServerWebExchange and GatewayFilterChain
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        ServerHttpRequest request = mock(ServerHttpRequest.class);
        HttpHeaders headers = mock(HttpHeaders.class);

        // Setting up the exchange to return the mocked request
        when(exchange.getRequest()).thenReturn(request);
        when(request.getURI()).thenReturn(URI.create("http://localhost:8080/test"));
        when(request.getMethod()).thenReturn(HttpMethod.GET);
        when(request.getHeaders()).thenReturn(headers);
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        // Apply the LoggingFilter and verify interactions
        loggingFilter.apply(new LoggingFilter.Config()).filter(exchange, chain);

        // Verifying that the expected methods were called
        verify(request, times(1)).getURI();
        verify(request, times(1)).getMethod();
        verify(request, times(1)).getHeaders();
        verify(chain, times(1)).filter(exchange);
    }
}
