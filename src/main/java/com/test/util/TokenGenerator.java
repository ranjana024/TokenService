package com.test.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class TokenGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateToken() {
        byte[] bytes = new byte[24]; // 32-char Base64 URL-safe string
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
