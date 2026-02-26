package com.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Value("${security.client.id}")
    private String clientId;

    @Value("${security.jwt.secret}")
    private String clientSecret;

    public boolean isValid(String id, String secret) {
        return clientId.equals(id) && clientSecret.equals(secret);
    }
}
