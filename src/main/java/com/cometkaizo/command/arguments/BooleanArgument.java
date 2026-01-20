package com.cometkaizo.command.arguments;

import java.util.function.Predicate;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Argument that translates user input into a boolean
 */
public class BooleanArgument extends Argument {
    /// Creates a new boolean argument
    public BooleanArgument(String name) {
        super(name);
    }
    /// Creates a new boolean argument
    public BooleanArgument(String name, Predicate<Object> requirement) {
        super(name, requirement);
    }

    /// Returns true if this argument accepts the given string
    @Override
    public boolean accepts(String string) {
        if (!string.equals("true") && !string.equals("false")) return false;
        boolean b = string.equals("true");
        return requirement.test(b);
    }

    /// Translates the given string into a boolean
    @Override
    public Boolean translate(String string) throws IllegalArgumentException {
        if (!accepts(string)) throw new IllegalArgumentException();
        return string.equals("true");
    }
}
