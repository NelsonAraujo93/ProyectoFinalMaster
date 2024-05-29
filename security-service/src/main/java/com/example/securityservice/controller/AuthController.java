package com.example.securityservice.controller;

import com.example.securityservice.config.UserConfig;
import com.example.securityservice.model.AuthRequest;
import com.example.securityservice.model.AuthResponse;
import com.example.securityservice.model.User;
import com.example.securityservice.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserConfig userConfig;

    @Autowired
    public AuthController(JwtService jwtService, UserConfig userConfig) {
        this.jwtService = jwtService;
        this.userConfig = userConfig;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthRequest authRequest) {
        Optional<User> userOpt = userConfig.getUsers().stream()
                .filter(user -> user.getUsername().equals(authRequest.getUsername()) && user.getPassword().equals(authRequest.getPassword()))
                .findFirst();

        if (userOpt.isPresent()) {
            User user = userOpt.get();
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
            AuthResponse response = new AuthResponse(username, jwtService.getRolesFromToken(token));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid token"));
        }
    }
}
