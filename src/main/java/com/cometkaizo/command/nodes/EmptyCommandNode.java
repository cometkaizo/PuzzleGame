package com.cometkaizo.command.nodes;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Command node that looks for nothing
 */
class EmptyCommandNode extends NoArgCommandNode {

    /// Creates a new empty command node
    protected EmptyCommandNode(EmptyCommandNodeBuilder builder) {
        super(builder);
    }

    /// Returns whether this command node accepts the argument (it always does)
    @Override
    protected boolean accepts() {
        return true;
    }

    /// Returns a string representation of this object
    @Override
    public String toString() {
        return "EmptyCommandNode{" +
                "level=" + level +
                '}';
    }
}
