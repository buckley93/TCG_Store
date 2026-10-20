package com.yourco.tcg.common;

import java.time.Instant;
import java.util.Map;

/**
 * TODO (record):
 * - Error response shape: status, code, message, timestamp, optional field errors.
 */
public record ApiError(int status, String code, String message, Instant timestamp, Map<String, String> fieldErrors) {

    public static ApiError of(int status, String code, String message, Map<String, String> fieldErrors) {
        return new ApiError(status, code, message, Instant.now(), fieldErrors);
    }

    public static ApiError of(int status, String code, String message) {
        return new ApiError(status, code, message, Instant.now(), null);
    }

}
