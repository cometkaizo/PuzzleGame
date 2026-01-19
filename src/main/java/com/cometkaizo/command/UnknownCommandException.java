package com.cometkaizo.command;

import java.util.List;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: An exception representing an unknown command name
 */
public class UnknownCommandException extends CommandSyntaxException {
    public UnknownCommandException(String commandName, List<List<String>> names) {
        this("Command '" + commandName + "' is not in this command group: \n" + names);
    }
    public UnknownCommandException(String message) {
        super(message);
    }

}
