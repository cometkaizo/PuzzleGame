package com.cometkaizo.command.nodes;

import com.cometkaizo.command.arguments.Argument;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Builder for the argument command node
 */
public class ArgumentCommandNodeBuilder extends CommandNodeBuilder {

    protected final Argument argument;

    public ArgumentCommandNodeBuilder(Argument argument) {
        this.argument = argument;
    }

    @Override
    protected ArgumentCommandNode build() {
        return new ArgumentCommandNode(this);
    }

    @Override
    public String toString() {
        return "ArgumentCommandNodeBuilder{" +
                "argument=" + argument +
                '}';
    }
}
