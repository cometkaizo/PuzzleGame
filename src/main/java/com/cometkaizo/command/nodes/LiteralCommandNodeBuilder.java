package com.cometkaizo.command.nodes;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Builder for a literal command node
 */
public class LiteralCommandNodeBuilder extends CommandNodeBuilder {

    protected final String literal;

    public LiteralCommandNodeBuilder(String literal) {
        this.literal = literal;
    }

    @Override
    protected LiteralCommandNode build() {
        return new LiteralCommandNode(this);
    }

    @Override
    public String toString() {
        return "LiteralCommandNodeBuilder{" +
                "literal='" + literal + '\'' +
                '}';
    }
}
