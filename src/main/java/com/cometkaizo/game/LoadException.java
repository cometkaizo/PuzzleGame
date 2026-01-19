package com.cometkaizo.game;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-27
 * Description: Exception when loading
 */
public class LoadException extends RuntimeException {
    public LoadException(String message) {
        super(message);
    }

    public LoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadException(Throwable cause) {
        super(cause);
    }

    public LoadException() {
    }
}
