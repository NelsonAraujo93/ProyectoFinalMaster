package com.example.apigateway.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.classic.Logger;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthFilterTests {

    private AuthFilter authFilter;
    private WebClient.Builder webClientBuilder;
    private ServerWebExchange exchange;
    private GatewayFilterChain chain;
    private ServerHttpRequest request;
    private ServerHttpResponse response;
    private WebClient webClient;
  
    private Logger logger;
    private TestAppender testAppender;

    class TestAppender extends AppenderBase<ILoggingEvent> {
        private final List<ILoggingEvent> events = new ArrayList<>();
    
        @Override
        protected void append(ILoggingEvent eventObject) {
            events.add(eventObject);
        }
    
        public boolean contains(String message, String level) {
            return events.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains(message)
                            && event.getLevel().toString().equals(level));
        }
    
        public List<ILoggingEvent> getEvents() {
            return events;
        }
    }

    @BeforeEach
    void setUp() {
        webClientBuilder = Mockito.mock(WebClient.Builder.class);
        authFilter = new AuthFilter(webClientBuilder);
        exchange = Mockito.mock(ServerWebExchange.class);
        chain = Mockito.mock(GatewayFilterChain.class);
        request = Mockito.mock(ServerHttpRequest.class);
        response = Mockito.mock(ServerHttpResponse.class);
        webClient = Mockito.mock(WebClient.class);

        when(exchange.getRequest()).thenReturn(request);
        when(exchange.getResponse()).thenReturn(response);
        when(chain.filter(exchange)).thenReturn(Mono.empty());
        when(webClientBuilder.build()).thenReturn(webClient);
        when(response.bufferFactory()).thenReturn(new DefaultDataBufferFactory());

         // Set up the logger and custom appender
        logger = (Logger) LoggerFactory.getLogger(AuthFilter.class);
        testAppender = new TestAppender();
        logger.addAppender(testAppender);
        testAppender.start();
    }

    @Test
    void testFilterWithMissingAuthorizationHeader() {
        when(request.getURI()).thenReturn(URI.create("/test"));
        when(request.getHeaders()).thenReturn(new HttpHeaders());

        when(response.setStatusCode(HttpStatus.UNAUTHORIZED)).thenReturn(true);
        when(response.writeWith(any())).thenReturn(Mono.empty());

        authFilter.apply(new AuthFilter.Config()).filter(exchange, chain).block();

        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verify(response).writeWith(any());

        // Verify that the expected log message was generated
        assertTrue(testAppender.contains("Bearer token is missing in header", "WARN"));
    }

    @Test
    void testFilterWithInvalidTokenFormat() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "InvalidToken");

        when(request.getURI()).thenReturn(URI.create("/test"));
        when(request.getHeaders()).thenReturn(headers);

        when(response.setStatusCode(HttpStatus.UNAUTHORIZED)).thenReturn(true);
        when(response.writeWith(any())).thenReturn(Mono.empty());

        authFilter.apply(new AuthFilter.Config()).filter(exchange, chain).block();

        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verify(response).writeWith(any());
    }
    
    @Test
    void testFilterWithBlacklistedToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer test-token");

        when(request.getURI()).thenReturn(URI.create("/test"));
        when(request.getHeaders()).thenReturn(headers);

        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenAnswer(invocation -> requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenAnswer(invocation -> requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenAnswer(invocation -> requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenAnswer(invocation -> responseSpec);

        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> responseSpec);
        when(responseSpec.bodyToMono(AuthFilter.AuthValidationResponse.class)).thenAnswer(invocation -> 
            Mono.just(new AuthFilter.AuthValidationResponse() {{
                setTokenBlacklisted(true);
            }})
        );

        when(response.setStatusCode(HttpStatus.UNAUTHORIZED)).thenAnswer(invocation -> true);
        when(response.writeWith(any())).thenAnswer(invocation -> Mono.empty());

        authFilter.apply(new AuthFilter.Config()).filter(exchange, chain).block();

        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verify(response).writeWith(any());
    }

    @Test
    void testFilterWithValidTokenAndSufficientRoles() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer test-token");
    
        when(request.getURI()).thenReturn(URI.create("/test"));
        when(request.getHeaders()).thenReturn(headers);
    
        ServerHttpRequest.Builder requestBuilder = Mockito.mock(ServerHttpRequest.Builder.class);
        when(request.mutate()).thenReturn(requestBuilder);
        when(requestBuilder.header(anyString(), anyString())).thenReturn(requestBuilder);
    
        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);
    
        when(webClient.get()).thenAnswer(invocation -> requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenAnswer(invocation -> requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenAnswer(invocation -> requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenAnswer(invocation -> responseSpec);
    
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> responseSpec);
        when(responseSpec.bodyToMono(AuthFilter.AuthValidationResponse.class)).thenReturn(Mono.just(new AuthFilter.AuthValidationResponse() {{
            setUserId(1L);
            setUsername("test-user");
            setRoles(Collections.singletonList("USER"));
            setTokenBlacklisted(false);
        }}));
    
        when(response.setStatusCode(HttpStatus.UNAUTHORIZED)).thenReturn(true);
        when(response.writeWith(any())).thenReturn(Mono.empty());
    
        authFilter.apply(new AuthFilter.Config(Collections.singletonList("USER"))).filter(exchange, chain).block();
    
        verify(chain, times(1)).filter(exchange);
        verify(request, times(1)).mutate();
        verify(requestBuilder, times(1)).header("x-auth-user-id", "1");
        verify(requestBuilder, times(1)).header("x-auth-user-name", "test-user");
        verify(requestBuilder, times(1)).header("x-auth-user-roles", "USER");
    }

    @Test
    void testSetRoles() {
        AuthFilter.Config config = new AuthFilter.Config();

        assertNull(config.getRoles());

        List<String> roles = Collections.singletonList("ADMIN");
        config.setRoles(roles);

        assertEquals(roles, config.getRoles());
    }

    @Test
    void testConfigConstructorWithRoles() {
        List<String> roles = Collections.singletonList("USER");
        AuthFilter.Config config = new AuthFilter.Config(roles);

        assertEquals(roles, config.getRoles());
    }

    @Test
    void testFilterWithAuthPath() {
        when(request.getURI()).thenReturn(URI.create("/auth/login"));
        when(request.getHeaders()).thenReturn(new HttpHeaders());

        Mono<Void> result = authFilter.apply(new AuthFilter.Config()).filter(exchange, chain);

        result.block(); 
        verify(chain, times(1)).filter(exchange);
        verify(response, never()).setStatusCode(any());
        verify(response, never()).writeWith(any());
    }

    @Test
    void testFilterWithEmptyToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "); // Empty token

        when(request.getURI()).thenReturn(URI.create("/test"));
        when(request.getHeaders()).thenReturn(headers);

        when(response.setStatusCode(HttpStatus.UNAUTHORIZED)).thenReturn(true);
        when(response.writeWith(any())).thenReturn(Mono.empty());

        authFilter.apply(new AuthFilter.Config()).filter(exchange, chain).block();

        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verify(response).writeWith(any());
        verify(chain, never()).filter(exchange);
    }

    @Test
    void testLoggingForBlacklistedToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer test-token");

        when(request.getURI()).thenReturn(URI.create("/test"));
        when(request.getHeaders()).thenReturn(headers);

        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenAnswer(invocation -> requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenAnswer(invocation -> requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenAnswer(invocation -> requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenAnswer(invocation -> responseSpec);

        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> responseSpec);
        when(responseSpec.bodyToMono(AuthFilter.AuthValidationResponse.class)).thenAnswer(invocation -> 
            Mono.just(new AuthFilter.AuthValidationResponse() {{
                setTokenBlacklisted(true);
            }})
        );

        when(response.writeWith(any())).thenAnswer(invocation -> Mono.empty());

        authFilter.apply(new AuthFilter.Config()).filter(exchange, chain).block();

        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verify(response).writeWith(any());

        // Verify that the expected log messages were generated
        assertTrue(testAppender.contains("Token is blacklisted", "WARN"));
    }

    @Test
    void testLoggingWhenUserIdIsNull() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer test-token");

        when(request.getURI()).thenReturn(URI.create("/test"));
        when(request.getHeaders()).thenReturn(headers);

        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenAnswer(invocation -> requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenAnswer(invocation -> requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenAnswer(invocation -> requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenAnswer(invocation -> responseSpec);

        // Ensure onStatus returns the responseSpec to allow chaining
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> responseSpec);

        // Return a response with a null userId
        when(responseSpec.bodyToMono(AuthFilter.AuthValidationResponse.class)).thenReturn(Mono.just(new AuthFilter.AuthValidationResponse() {{
            setUserId(null); // Simulate null userId
            setRoles(Collections.singletonList("USER"));
        }}));

        when(response.setStatusCode(HttpStatus.UNAUTHORIZED)).thenAnswer(invocation -> true);
        when(response.writeWith(any())).thenAnswer(invocation -> Mono.empty());

        // Execute the filter method
        authFilter.apply(new AuthFilter.Config(Collections.singletonList("USER"))).filter(exchange, chain).block();

        // Verify the response handling
        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verify(response).writeWith(any());
        verify(chain, never()).filter(exchange);

        // Verify the log message
        assertTrue(testAppender.contains("Invalid token: no user ID found", "WARN"), "Expected log message not found");

        // Print all captured log events for debugging
        for (ILoggingEvent event : testAppender.getEvents()) {
            System.out.println("Captured log: " + event.getLevel() + " - " + event.getFormattedMessage());
        }
    }


    @Test
    void testLoggingWhenRolesAreInsufficient() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer test-token");

        when(request.getURI()).thenReturn(URI.create("/test"));
        when(request.getHeaders()).thenReturn(headers);

        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenAnswer(invocation -> requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenAnswer(invocation -> requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenAnswer(invocation -> requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenAnswer(invocation -> responseSpec);

        // Ensure onStatus returns the responseSpec to allow chaining
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> responseSpec);

        when(responseSpec.bodyToMono(AuthFilter.AuthValidationResponse.class)).thenReturn(Mono.just(new AuthFilter.AuthValidationResponse() {{
            setUserId(1L);
            setRoles(Collections.singletonList("USER")); // Simulate roles that do not match config
        }}));

        when(response.setStatusCode(HttpStatus.UNAUTHORIZED)).thenAnswer(invocation -> true);
        when(response.writeWith(any())).thenAnswer(invocation -> Mono.empty());

        authFilter.apply(new AuthFilter.Config(Collections.singletonList("ADMIN"))).filter(exchange, chain).block();

        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verify(response).writeWith(any());
        verify(chain, never()).filter(exchange);

        // Verify the log message
        assertTrue(testAppender.contains("Insufficient role for user: 1", "WARN"),
                "Expected log message not found");

        // Print all captured log events for debugging
        for (ILoggingEvent event : testAppender.getEvents()) {
            System.out.println("Captured log: " + event.getLevel() + " - " + event.getFormattedMessage());
        }
    }

    @Test
    void testWebClientLoggingOnErrorStatus() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer test-token");

        when(request.getURI()).thenReturn(URI.create("/test"));
        when(request.getHeaders()).thenReturn(headers);

        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenAnswer(invocation -> requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenAnswer(invocation -> requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenAnswer(invocation -> requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenAnswer(invocation -> responseSpec);

        // Mock the onStatus method to return the responseSpec for chaining and simulate the error
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            logger.error("Received error status: {}", HttpStatus.INTERNAL_SERVER_ERROR);
            return responseSpec;
        });

        // Mock bodyToMono to simulate the error after the onStatus method has been invoked
        when(responseSpec.bodyToMono(AuthFilter.AuthValidationResponse.class))
                .thenAnswer(invocation -> Mono.error(new RuntimeException("UNAUTHORIZED")));

        when(response.writeWith(any())).thenAnswer(invocation -> Mono.empty());

        authFilter.apply(new AuthFilter.Config()).filter(exchange, chain).block();

        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verify(response).writeWith(any());

        // Modify the assertions to match the log output
        assertTrue(testAppender.contains("Received error status: 500 INTERNAL_SERVER_ERROR", "ERROR"));
        assertTrue(testAppender.contains("Token validation error:", "ERROR"));
    }
    
}
