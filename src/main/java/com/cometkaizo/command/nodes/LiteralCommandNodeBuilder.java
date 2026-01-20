package com.cometkaizo.command.nodes;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Builder for a literal command node
 */
public class LiteralCommandNodeBuilder extends CommandNodeBuilder {

    protected final String literal;

    /// Creates a new literal command node builder
    public LiteralCommandNodeBuilder(String literal) {
        this.literal = literal;
    }

    /// Builds this command node
    @Override
    protected LiteralCommandNode build() {
        return new LiteralCommandNode(this);
    }

    /// Returns a string representation of this object
    @Override
    public String toString() {
        return "LiteralCommandNodeBuilder{" +
                "literal='" + literal + '\'' +
                '}';
    }
}
