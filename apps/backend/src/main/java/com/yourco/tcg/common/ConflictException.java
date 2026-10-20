package com.yourco.tcg.common;

/**
 * TODO (class):
 * - extends RuntimeException. Maps to 409.
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
