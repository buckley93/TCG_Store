package com.yourco.tcg.common;

/**
 * TODO (class):
 * - extends RuntimeException. Maps to 409 with a stock-specific error code.
 */
public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String message) {
        super(message);
    }
}
