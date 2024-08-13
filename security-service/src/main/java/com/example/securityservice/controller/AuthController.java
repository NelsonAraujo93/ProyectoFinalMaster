package com.example.securityservice.controller;

import com.example.securityservice.dto.PymeDTO;
import com.example.securityservice.dto.UserDTO;
import com.example.securityservice.model.AuthRequest;
import com.example.securityservice.model.AuthResponse;
import com.example.securityservice.model.AuthResponseUser;
import com.example.securityservice.model.User;
import com.example.securityservice.service.BlacklistedTokenService;
import com.example.securityservice.service.JwtService;
import com.example.securityservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    public ResponseEntity<AuthResponseUser> authenticate(@RequestBody AuthRequest authRequest) {
        User user = userService.findByUsername(authRequest.getUsername()).orElse(null);
        if (user != null && passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getRoleNames());

            UserDTO userDTO = userService.convertToUserDto(user);

            AuthResponseUser authResponse = new AuthResponseUser(token, userDTO, false);
            return ResponseEntity.ok(authResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponseUser("Invalid username or password", true));
        }
    }

    @PostMapping("/register/client")
    public ResponseEntity<Object> registerClient(@RequestBody UserDTO userDTO) {
        return registerUser(userDTO, "CLIENT");
    }

    @PostMapping("/register/pyme")
    public ResponseEntity<Object> registerPyme(@RequestBody PymeDTO pymeDTO) {
        return registerPymeUser(pymeDTO);
    }

    private ResponseEntity<Object> registerUser(UserDTO userDTO, String role) {
        if (userService.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", "Username is already taken"));
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setEnabled(true);
        userDTO.setRoles(Collections.singletonList(role)); // Add role to DTO

        userService.saveUser(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "User created successfully with role: " + role));
    }

    private ResponseEntity<Object> registerPymeUser(PymeDTO pymeDTO) {
        if (userService.existsByUsername(pymeDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", "Username is already taken"));
        }

        pymeDTO.setPassword(passwordEncoder.encode(pymeDTO.getPassword()));
        pymeDTO.setEnabled(true);
        pymeDTO.setRoles(Collections.singletonList("PYME")); // Add role to DTO

        userService.savePyme(pymeDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "Pyme created successfully with role: PYME"));
    }

    @GetMapping("/validateToken")
    public ResponseEntity<AuthResponse> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        if (blacklistedTokenService.isTokenBlacklisted(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Token is blacklisted", true));
        }
        boolean isValid = jwtService.validateToken(token);
        if (isValid) {
            Long userId = jwtService.getUserIdFromToken(token);
            String username = jwtService.getUsernameFromToken(token);
            List<String> roles = jwtService.getRolesFromToken(token);
            AuthResponse response = new AuthResponse(userId, username, roles, false);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid token", true));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        blacklistedTokenService.blacklistToken(token);
        return ResponseEntity.ok("Logged out successfully");
    }
}
