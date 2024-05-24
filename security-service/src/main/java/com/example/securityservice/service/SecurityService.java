package com.example.securityservice.service;

import com.example.securityservice.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SecurityService implements UserDetailsService {

    @Autowired
    private JWTUtil utilidadJWT;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Mock user details, you can implement fetching user details from database
        if ("user".equals(username)) {
            return User.withUsername("user").password("{noop}password").roles("USER").build();
        } else if ("admin".equals(username)) {
            return User.withUsername("admin").password("{noop}password").roles("ADMIN").build();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public Mono<Boolean> validarToken(String token) {
        try {
            String username = utilidadJWT.extraerNombreUsuario(token);
            return Mono.just(utilidadJWT.validarToken(token, username));
        } catch (Exception e) {
            return Mono.just(false);
        }
    }
}
