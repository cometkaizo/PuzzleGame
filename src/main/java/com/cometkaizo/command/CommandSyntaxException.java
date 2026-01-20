package com.cometkaizo.command;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: An exception representing incorrect syntax for a command
 */
public class CommandSyntaxException extends RuntimeException {
    /// Creates a new command syntax exception
    public CommandSyntaxException(String message) {
        super(message);
    }

}
