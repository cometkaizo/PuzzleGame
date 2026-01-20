package com.cometkaizo.command.arguments;

import java.util.function.Predicate;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Argument that translates user input into a string
 */
public class StringArgument extends Argument {

    /// Creates a new string argument
    public StringArgument(String name) {
        super(name);
    }
    /// Creates a new string argument
    public StringArgument(String name, Predicate<String> requirement) {
        super(name, o -> requirement.test((String) o));
    }

    /// Returns true if this argument accepts the given string
    @Override
    public boolean accepts(String string) {
        return requirement.test(string);
    }

    /// Translates the given string into a string
    @Override
    public String translate(String string) throws IllegalArgumentException {
        if (!accepts(string)) throw new IllegalArgumentException();
        return string;
    }
}
