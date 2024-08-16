package com.example.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            logger.info("Request path: {}", exchange.getRequest().getURI().getPath());
            logger.info("Request method: {}", exchange.getRequest().getMethod());
            logger.info("Request headers: {}", exchange.getRequest().getHeaders());
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Configuration properties if any
    }
}
