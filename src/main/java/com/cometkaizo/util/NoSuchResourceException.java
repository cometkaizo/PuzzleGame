package com.cometkaizo.util;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-19
 * Description: Exception representing the lack of a wanted resource
 */
public class NoSuchResourceException extends RuntimeException {
    /// Creates a new exception
    public NoSuchResourceException(String message) {
        super(message);
    }

    /// Creates a new exception
    public NoSuchResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    /// Creates a new exception
    public NoSuchResourceException(Throwable cause) {
        super(cause);
    }

    /// Creates a new exception
    public NoSuchResourceException() {
    }
}
