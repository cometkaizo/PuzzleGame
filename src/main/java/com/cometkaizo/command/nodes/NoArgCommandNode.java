package com.cometkaizo.command.nodes;

/**
 * Author: Andy Wang
 * Date Modified: 2025-01-19
 * Description: Represents classes that do not need an argument to determine its functionality
 */
abstract class NoArgCommandNode extends CommandNode {

    /// Creates a new command node that does not accept arguments
    protected NoArgCommandNode(NoArgCommandNodeBuilder builder) {
        super(builder);
    }

    /// Returns whether this node accepts the given argument
    @Override
    protected final boolean accepts(String arg) {
        return accepts();
    }

    /// Returns whether this command node should pass (no-arg version of accepts(arg))
    protected abstract boolean accepts();

    /// Executes functionality (none)
    @Override
    protected final void executeFunctionality() {

    }
}
