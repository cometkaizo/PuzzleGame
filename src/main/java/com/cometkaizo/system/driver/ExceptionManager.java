package com.cometkaizo.system.driver;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Handler for exceptions and errors
 */
public interface ExceptionManager {
    Throwable handleException(Exception e);
    Throwable handleError(Error err);
}
