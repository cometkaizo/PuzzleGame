package com.cometkaizo.util;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-19
 * Description: Exception representing the lack of a wanted resource
 */
public class NoSuchResourceException extends RuntimeException {
    public NoSuchResourceException(String message) {
        super(message);
    }

    public NoSuchResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchResourceException(Throwable cause) {
        super(cause);
    }

    public NoSuchResourceException() {
    }
}
