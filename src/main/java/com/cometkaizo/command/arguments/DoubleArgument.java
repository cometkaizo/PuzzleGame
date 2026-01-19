package com.cometkaizo.command.arguments;

import java.util.function.Predicate;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Argument that translates user input into a double
 */
public class DoubleArgument extends Argument {
    public DoubleArgument(String name) {
        super(name);
    }
    public DoubleArgument(String name, Predicate<Object> requirement) {
        super(name, requirement);
    }

    @Override
    public boolean accepts(String string) {
        return string.matches("^[-+]?\\d+(\\.\\d+)?$") && requirement.test(Double.parseDouble(string));
    }

    @Override
    public Double translate(String string) throws IllegalArgumentException {
        if (!accepts(string)) throw new IllegalArgumentException();
        return Double.parseDouble(string);
    }
}
