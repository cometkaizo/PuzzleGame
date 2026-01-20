package com.cometkaizo.command.nodes;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Root command node of a command
 */
class RootCommandNode extends CommandNode {

    /// Creates a new root command node
    public RootCommandNode(RootCommandNodeBuilder builder) {
        super(builder);
    }


    /// Returns whether this command node accept the given argument
    @Override
    protected boolean accepts(String arg) {
        return true;
    }

    /// Executes functionality (none)
    @Override
    protected void executeFunctionality() {

    }
}
