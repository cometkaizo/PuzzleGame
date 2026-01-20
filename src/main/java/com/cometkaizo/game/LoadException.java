package com.cometkaizo.game;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-27
 * Description: Exception when loading
 */
public class LoadException extends RuntimeException {
    /// Creates a new exception
    public LoadException(String message) {
        super(message);
    }

    /// Creates a new exception
    public LoadException(String message, Throwable cause) {
        super(message, cause);
    }

    /// Creates a new exception
    public LoadException(Throwable cause) {
        super(cause);
    }

    /// Creates a new exception
    public LoadException() {
    }
}
