package com.cometkaizo.command.nodes;

/**
 * Author: Andy Wang
 * Date Modified: 2025-01-19
 * Description: Represents classes that do not need an argument to determine its functionality
 */
abstract class NoArgCommandNode extends CommandNode {

    protected NoArgCommandNode(NoArgCommandNodeBuilder builder) {
        super(builder);
    }

    @Override
    protected final boolean accepts(String arg) {
        return accepts();
    }

    protected abstract boolean accepts();

    @Override
    protected final void executeFunctionality() {

    }
}
