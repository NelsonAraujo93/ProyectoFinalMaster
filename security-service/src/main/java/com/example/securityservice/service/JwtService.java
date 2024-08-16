package com.example.securityservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(Long userId, String username, List<String> roles) {
        try {
            String token = JWT.create()
                    .withSubject(username)
                    .withClaim("userId", userId)
                    .withClaim("roles", roles)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                    .sign(Algorithm.HMAC512(secret));
            logger.info("Generated token: {}", token);
            return token;
        } catch (Exception e) {
            logger.error("Error generating token: {}", e.getMessage());
            return null;
        }
    }

    public Boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            logger.info("Token is valid");
            return true;
        } catch (JWTVerificationException e) {
            logger.error("Token validation error: {}", e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            String username = jwt.getSubject();
            logger.info("Username from token: {}", username);
            return username;
        } catch (JWTDecodeException e) {
            logger.error("Error decoding token to get username: {}", e.getMessage());
            return null;
        }
    }

    public Long getUserIdFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Long userId = jwt.getClaim("userId").asLong();
            logger.info("User ID from token: {}", userId);
            return userId;
        } catch (JWTDecodeException e) {
            logger.error("Error decoding token to get user ID: {}", e.getMessage());
            return null;
        }
    }

    public List<String> getRolesFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            List<String> roles = jwt.getClaim("roles").asList(String.class);
            logger.info("Roles from token: {}", roles);
            return roles;
        } catch (JWTDecodeException e) {
            logger.error("Error decoding token to get roles: {}", e.getMessage());
            return null;
        }
    }
}
