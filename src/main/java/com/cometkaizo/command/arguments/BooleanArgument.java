package com.cometkaizo.command.arguments;

import java.util.function.Predicate;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Argument that translates user input into a boolean
 */
public class BooleanArgument extends Argument {
    public BooleanArgument(String name) {
        super(name);
    }
    public BooleanArgument(String name, Predicate<Object> requirement) {
        super(name, requirement);
    }

    @Override
    public boolean accepts(String string) {
        if (!string.equals("true") && !string.equals("false")) return false;
        boolean b = string.equals("true");
        return requirement.test(b);
    }

    @Override
    public Boolean translate(String string) throws IllegalArgumentException {
        if (!accepts(string)) throw new IllegalArgumentException();
        return string.equals("true");
    }
}
