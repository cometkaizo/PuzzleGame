package com.cometkaizo.command.arguments;

import java.util.function.Predicate;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Argument that translates user input into an object
 */
public abstract class ObjectArgument extends Argument {

    public ObjectArgument(String name) {
        super(name);
    }
    public ObjectArgument(String name, Predicate<Object> requirement) {
        super(name, requirement);
    }

}
