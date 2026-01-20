package com.cometkaizo.command.arguments;

import java.util.function.Predicate;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Argument that translates user input into an int
 */
public class IntArgument extends Argument {

    /// Creates a new int argument
    public IntArgument(String name) {
        super(name);
    }
    /// Creates a new int argument
    public IntArgument(String name, Predicate<Object> requirement) {
        super(name, requirement);
    }

    /// Returns true if this argument accepts the given string
    @Override
    public boolean accepts(String s) {
        return s.matches("[-+]?\\d+") && requirement.test(Integer.parseInt(s));
    }

    /// Translates the given string into an int
    @Override
    public Integer translate(String string) throws IllegalArgumentException {
        if (!accepts(string)) throw new IllegalArgumentException();
        return Integer.parseInt(string);
    }

    /// Turns this object into a string
    @Override
    public String toString() {
        return "IntArgument{" +
                "name='" + name + '\'' +
                ", requirement=" + requirement +
                '}';
    }
}
