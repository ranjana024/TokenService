package com.test.exception;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String token) {
        super("No account number found for token: " + token);
    }
}
