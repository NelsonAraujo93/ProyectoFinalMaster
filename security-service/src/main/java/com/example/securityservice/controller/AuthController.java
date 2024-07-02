package com.example.securityservice.controller;

import com.example.securityservice.model.AuthRequest;
import com.example.securityservice.model.AuthResponse;
import com.example.securityservice.model.User;
import com.example.securityservice.service.BlacklistedTokenService;
import com.example.securityservice.service.JwtService;
import com.example.securityservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final BlacklistedTokenService blacklistedTokenService;

    @Autowired
    public AuthController(JwtService jwtService, UserService userService, PasswordEncoder passwordEncoder, BlacklistedTokenService blacklistedTokenService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.blacklistedTokenService = blacklistedTokenService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthRequest authRequest) {
        User user = userService.findByUsername(authRequest.getUsername());
        if (user != null && passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(user.getUsername(), user.getRoles());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @GetMapping("/validateToken")
    public ResponseEntity<AuthResponse> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        boolean isValid = jwtService.validateToken(token);
        if (isValid) {
            String username = jwtService.getUsernameFromToken(token);
            List<String> roles = jwtService.getRolesFromToken(token);
            AuthResponse response = new AuthResponse(username, roles);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid token"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        blacklistedTokenService.blacklistToken(token);
        return ResponseEntity.ok("Logged out successfully");
    }
}
