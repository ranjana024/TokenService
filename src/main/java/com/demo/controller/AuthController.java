package com.demo.controller;

import com.demo.dto.TokenRequest;
import com.demo.dto.TokenResponse;
import com.demo.service.AuthenticationService;
import com.demo.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationService clientAuthService, JwtUtil jwtUtil) {
        this.authenticationService = clientAuthService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@Valid @RequestBody TokenRequest request) {

        if (!authenticationService.isValid(request.clientId(), request.clientSecret())) {
            return ResponseEntity.status(401).body("Invalid client credentials");
        }

        String token = jwtUtil.generateToken(request.clientId());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}