package com.example.securityservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private Logger logger;

    private final String secret = "testSecret";
    private final long expiration = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    
        jwtService = new JwtService();
        jwtService.setSecret(secret); // Use setter to inject secret
        jwtService.setExpiration(expiration); // Use setter to inject expiration
    }

    @Test
    void testGenerateToken() {
        Long userId = 1L;
        String username = "testUser";
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");

        String token = jwtService.generateToken(userId, username, roles);

        assertNotNull(token);
    }

    @Test
    void testValidateToken() {
        String token = JWT.create()
                .withSubject("testUser")
                .withClaim("userId", 1L)
                .withClaim("roles", Arrays.asList("ROLE_USER"))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                .sign(Algorithm.HMAC512("testSecret"));

        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void testGetUsernameFromToken() {
        String token = JWT.create()
                .withSubject("testUser")
                .sign(Algorithm.HMAC512("testSecret"));

        String username = jwtService.getUsernameFromToken(token);

        assertEquals("testUser", username);
    }

    @Test
    void testGetUserIdFromToken() {
        String token = JWT.create()
                .withClaim("userId", 1L)
                .sign(Algorithm.HMAC512("testSecret"));

        Long userId = jwtService.getUserIdFromToken(token);

        assertEquals(1L, userId);
    }

    @Test
    void testGetRolesFromToken() {
        String token = JWT.create()
                .withClaim("roles", Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                .sign(Algorithm.HMAC512("testSecret"));

        List<String> roles = jwtService.getRolesFromToken(token);

        assertEquals(Arrays.asList("ROLE_USER", "ROLE_ADMIN"), roles);
    }

    @Test
    void testValidateTokenInvalidToken() {
        assertFalse(jwtService.validateToken("invalidToken"));
    }

    @Test
    void testGetUsernameFromTokenInvalidToken() {
        assertNull(jwtService.getUsernameFromToken("invalidToken"));
    }

    @Test
    void testGetUserIdFromTokenInvalidToken() {
        assertNull(jwtService.getUserIdFromToken("invalidToken"));
    }

    @Test
    void testGetRolesFromTokenInvalidToken() {
        assertNull(jwtService.getRolesFromToken("invalidToken"));
    }
}
