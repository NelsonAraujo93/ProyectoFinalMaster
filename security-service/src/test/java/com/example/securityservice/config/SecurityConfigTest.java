package com.example.securityservice.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class SecurityConfigTest {

  @Mock
  private ServerHttpSecurity serverHttpSecurity;

  @InjectMocks
  private SecurityConfig securityConfig;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testSecurityWebFilterChainConfiguration() {
    // Mock behavior
    when(serverHttpSecurity.csrf(any())).thenAnswer(invocation -> serverHttpSecurity);
    when(serverHttpSecurity.formLogin(any())).thenAnswer(invocation -> serverHttpSecurity);
    when(serverHttpSecurity.logout(any())).thenAnswer(invocation -> serverHttpSecurity);
    when(serverHttpSecurity.authorizeExchange(any())).thenAnswer(invocation -> serverHttpSecurity);
    when(serverHttpSecurity.exceptionHandling(any())).thenAnswer(invocation -> serverHttpSecurity);

    // Call the method to be tested
    SecurityWebFilterChain result = securityConfig.securityWebFilterChain(serverHttpSecurity);
    // Verify interactions
    verify(serverHttpSecurity).csrf(any());
    verify(serverHttpSecurity).formLogin(any());
    verify(serverHttpSecurity).logout(any());
    verify(serverHttpSecurity).authorizeExchange(any());
    verify(serverHttpSecurity).exceptionHandling(any());

    // Verify the build method was called
    serverHttpSecurity.authorizeExchange(exchanges -> exchanges.pathMatchers("/auth/**").permitAll().anyExchange().authenticated());

    serverHttpSecurity.exceptionHandling(handling-> handling
      .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)
      ))
      .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN)
      ))
    );

    // Assert the result
    verify(serverHttpSecurity).build();
  }

  @Test
  public void testPasswordEncoderBean() {
    PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
    assertNotNull(passwordEncoder);
    assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
  }
}
