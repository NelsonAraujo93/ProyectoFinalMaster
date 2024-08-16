package com.example.apigateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import java.util.List;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    private final WebClient.Builder webClientBuilder;

    @Value("${api.security.url}")
    private String seguridadUrl;

    @Autowired
    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpResponse response = exchange.getResponse();
            String path = exchange.getRequest().getURI().getPath();
            logger.info("Processing request for path: {}", path);

            if (path.startsWith("/auth")) {
                return chain.filter(exchange);
            }
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                logger.warn("Bearer token is missing in header");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.writeWith(Mono.just(response.bufferFactory().wrap("Bearer token is missing in header".getBytes())));
            }

            @SuppressWarnings("null")
            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (!authHeader.startsWith("Bearer ")) {
                logger.warn("Bearer token is not present");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.writeWith(Mono.just(response.bufferFactory().wrap("Bearer token is not present".getBytes())));
            }

            String token = authHeader.replace("Bearer ", "");
            if (token.isEmpty()) {
                logger.warn("Token is empty");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.writeWith(Mono.just(response.bufferFactory().wrap("Token is empty".getBytes())));
            }

            logger.info("Token to validate: {}", token);

            return webClientBuilder.build()
                    .get()
                    .uri(seguridadUrl + "/auth/validateToken")
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .retrieve()
                    .onStatus(httpStatus -> {
                        logger.error("Received error status: {}", httpStatus);
                        return httpStatus.value() != HttpStatus.OK.value();
                    }, error -> {
                        logger.error("Error during token validation: {}", error);
                        return Mono.error(new Throwable("UNAUTHORIZED"));
                    })
                    .bodyToMono(AuthValidationResponse.class)
                    .flatMap(authValidationResponse -> {
                        logger.info("Token validated for user: {}", authValidationResponse.getUserId());
                        logger.info("User roles: {}", authValidationResponse.getRoles());
                        logger.info("Required role: {}", config.getRoles());

                        if (authValidationResponse.isTokenBlacklisted()) {
                            logger.warn("Token is blacklisted");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED);
                            return response.writeWith(Mono.just(response.bufferFactory().wrap("Token is blacklisted".getBytes())));
                        }

                        if (authValidationResponse.getUserId() == null) {
                            logger.warn("Invalid token: no user ID found");
                            response.setStatusCode(HttpStatus.UNAUTHORIZED);
                            return response.writeWith(Mono.just(response.bufferFactory().wrap("Invalid token".getBytes())));
                        }

                        if (config.getRoles().stream().noneMatch(authValidationResponse.getRoles()::contains)) {
                            logger.warn("Insufficient role for user: {}", authValidationResponse.getUserId());
                            response.setStatusCode(HttpStatus.UNAUTHORIZED);
                            return response.writeWith(Mono.just(response.bufferFactory().wrap("Insufficient role".getBytes())));
                        }

                        exchange.getRequest().mutate()
                                .header("x-auth-user-id", String.valueOf(authValidationResponse.getUserId()))
                                .header("x-auth-user-name", authValidationResponse.getUsername())
                                .header("x-auth-user-roles", String.join(",", authValidationResponse.getRoles()));

                        return chain.filter(exchange);
                    }).onErrorResume(e -> {
                        logger.error("Token validation error: ", e);
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        return response.writeWith(Mono.just(response.bufferFactory().wrap(("Authorization error: " + e.getMessage()).getBytes())));
                    });
        };
    }

    public static class Config {
        private List<String> roles;
    
        public Config() {
        }
    
        public Config(List<String> roles) {
            this.roles = roles;
        }
    
        public List<String> getRoles() {
            return roles;
        }
    
        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }
    
    public static class AuthValidationResponse {
        private Long userId;
        private String username;
        private List<String> roles;
        private boolean isTokenBlacklisted;

        // Getters and Setters
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public boolean isTokenBlacklisted() {
            return isTokenBlacklisted;
        }

        public void setTokenBlacklisted(boolean tokenBlacklisted) {
            isTokenBlacklisted = tokenBlacklisted;
        }
    }
}