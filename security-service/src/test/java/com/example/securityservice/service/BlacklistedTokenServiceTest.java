package com.example.securityservice.service;

import com.example.securityservice.repository.BlacklistedTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class BlacklistedTokenServiceTest {

  @InjectMocks
  private BlacklistedTokenService blacklistedTokenService;

  @Mock
  private BlacklistedTokenRepository blacklistedTokenRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testBlacklistToken() {
    String token = "testToken";

    // When
    blacklistedTokenService.blacklistToken(token);

    // Then
    verify(blacklistedTokenRepository).save(argThat(blacklistedToken -> token.equals(blacklistedToken.getToken())));
  }

  @Test
  void testIsTokenBlacklisted_TokenExists() {
    String token = "testToken";

    when(blacklistedTokenRepository.existsByToken(token)).thenReturn(true);

    // When
    boolean result = blacklistedTokenService.isTokenBlacklisted(token);

    // Then
    assertTrue(result);
    verify(blacklistedTokenRepository).existsByToken(token);
  }

  @Test
  void testIsTokenBlacklisted_TokenDoesNotExist() {
    String token = "testToken";

    when(blacklistedTokenRepository.existsByToken(token)).thenReturn(false);

    // When
    boolean result = blacklistedTokenService.isTokenBlacklisted(token);

    // Then
    assertFalse(result);
    verify(blacklistedTokenRepository).existsByToken(token);
  }
}
