package com.cometkaizo.command.nodes;

import com.cometkaizo.command.arguments.Argument;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Builder for the argument command node
 */
public class ArgumentCommandNodeBuilder extends CommandNodeBuilder {

    protected final Argument argument;

    /// Creates a new command node builder
    public ArgumentCommandNodeBuilder(Argument argument) {
        this.argument = argument;
    }

    /// Builds this object into a command node
    @Override
    protected ArgumentCommandNode build() {
        return new ArgumentCommandNode(this);
    }

    /// Turns this object into a string
    @Override
    public String toString() {
        return "ArgumentCommandNodeBuilder{" +
                "argument=" + argument +
                '}';
    }
}
