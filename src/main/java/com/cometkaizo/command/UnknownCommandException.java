package com.cometkaizo.command;

import java.util.List;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: An exception representing an unknown command name
 */
public class UnknownCommandException extends CommandSyntaxException {
    /// Creates a new unknown command exception
    public UnknownCommandException(String commandName, List<List<String>> names) {
        this("Command '" + commandName + "' is not in this command group: \n" + names);
    }
    /// Creates a new unknown command exception
    public UnknownCommandException(String message) {
        super(message);
    }

}
