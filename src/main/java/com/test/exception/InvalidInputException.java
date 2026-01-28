package com.test.exception;

/**
 * Thrown when the client provides invalid or malformed input.
 */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super("Invalid Input");
    }
}