package com.yourco.tcg.common;

/**
 * TODO (class):
 * - extends RuntimeException. Maps to 404.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
