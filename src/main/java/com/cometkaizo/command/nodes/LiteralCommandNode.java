package com.cometkaizo.command.nodes;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Command node that looks for a literal word
 */
class LiteralCommandNode extends CommandNode {

    private final String literal;

    /// Returns whether the argument is the wanted literal
    protected boolean accepts(String string) {
        return string.equals(literal);
    }

    /// Executes functionality (none, for this command node)
    @Override
    protected void executeFunctionality() {

    }

    /// Creates a new literal command node
    public LiteralCommandNode(LiteralCommandNodeBuilder builder) {
        super(builder);
        this.literal = builder.literal;
    }

    /// Returns a string representation of this object
    @Override
    public String toString() {
        return "LiteralCommandNode{" +
                "literal='" + literal + '\'' +
                '}';
    }

    /// Returns a user-displayable string representation of this object
    @Override
    public String toPrettyString() {
        return literal;
    }
}
