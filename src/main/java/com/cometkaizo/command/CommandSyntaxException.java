package com.cometkaizo.command;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: An exception representing incorrect syntax for a command
 */
public class CommandSyntaxException extends RuntimeException {
    public CommandSyntaxException(String message) {
        super(message);
    }

}
