package com.example.securityservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.securityservice.repository.BlacklistedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @Value("${jwt.issuer}")
    private String issuer;

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    public String generateToken(String username, List<String> roles) {
        return JWT.create()
                .withSubject(username)
                .withIssuer(issuer)
                .withClaim("roles", roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC512(secretKey.getBytes()));
    }

    public boolean validateToken(String token) {
        try {
            // Verificar si el token está en la lista negra
            if (isTokenBlacklisted(token)) {
                return false;
            }
            JWT.require(Algorithm.HMAC512(secretKey.getBytes()))
                .withIssuer(issuer)
                .build()
                .verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(secretKey.getBytes()))
                .withIssuer(issuer)
                .build()
                .verify(token)
                .getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(secretKey.getBytes()))
                .withIssuer(issuer)
                .build()
                .verify(token)
                .getClaim("roles")
                .asList(String.class);
    }

    private boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.findByToken(token).isPresent();
    }
}
