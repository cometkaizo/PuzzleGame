package com.cometkaizo.command.nodes;

import com.cometkaizo.command.arguments.Argument;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Command node that looks for an argument
 */
class ArgumentCommandNode extends CommandNode {

    private final Argument argument;

    /// Creates a new command node
    public ArgumentCommandNode(ArgumentCommandNodeBuilder builder) {
        super(builder);
        this.argument = builder.argument;
    }

    /// Returns whether this command node accepts the given argument
    @Override
    protected boolean accepts(String arg) {
        return argument.accepts(arg);
    }

    /// Parses the arguments and puts them into the map
    @Override
    protected void executeFunctionality() {
        context.parsedArgs.put(argument.getName(), argument.translate(context.args[level]));
    }

    /// Turns this object into a string
    @Override
    public String toString() {
        return "ArgumentCommandNode{" +
                "argument=" + argument +
                '}';
    }

    /// Returns a user-displayable string
    @Override
    public String toPrettyString() {
        return argument.toPrettyString() + " ARGUMENT '" + argument.getName() + "'";
    }
}
