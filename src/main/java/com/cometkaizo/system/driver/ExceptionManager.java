package com.cometkaizo.system.driver;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Handler for exceptions and errors
 */
public interface ExceptionManager {
    /// Called when an exception occurs
    Throwable handleException(Exception e);
    /// Called when an error occurs
    Throwable handleError(Error err);
}
