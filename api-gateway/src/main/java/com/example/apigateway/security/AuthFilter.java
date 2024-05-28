package com.example.apigateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
        System.out.println("AuthFilter constructor called");
    }

    @Override
    public GatewayFilter apply(Config config) {
        System.out.println("AuthFilter apply method called");
        logger.info("AuthFilter applied");
        return (exchange, chain) -> {

            ServerHttpResponse response = exchange.getResponse();
            String path = exchange.getRequest().getURI().getPath();

            // Skip authentication for /auth/** paths
            if (path.startsWith("/auth")) {
                logger.info("Skipping authentication for path: " + path);
                return chain.filter(exchange);
            }

            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (!authHeader.startsWith("Bearer ")) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            try {
                logger.info("Performing authentication...");
                return webClientBuilder.build()
                        .get()
                        .uri("http://localhost:8083/auth/validateToken")  // URL to validate token in security-service
                        .header(HttpHeaders.AUTHORIZATION, authHeader)
                        .retrieve()
                        .onStatus(httpStatus -> httpStatus.value() != HttpStatus.OK.value(),
                                error -> Mono.error(new Throwable("UNAUTHORIZED")))
                        .toEntity(String.class)
                        .flatMap(entity -> {
                            if (entity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                                return response.setComplete();
                            }
                            exchange.getRequest()
                                    .mutate()
                                    .header("x-auth-user-id", entity.getBody());
                            return chain.filter(exchange);
                        });
            } catch (Throwable e) {
                logger.error("Error during authentication", e);
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        };
    }

    public static class Config {
        // Configuration properties if any
    }
}
