package com.cometkaizo.command.nodes;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Command node that looks for nothing
 */
class EmptyCommandNode extends NoArgCommandNode {

    protected EmptyCommandNode(EmptyCommandNodeBuilder builder) {
        super(builder);
    }

    @Override
    protected boolean accepts() {
        return true;
    }

    @Override
    public String toString() {
        return "EmptyCommandNode{" +
                "level=" + level +
                '}';
    }
}
