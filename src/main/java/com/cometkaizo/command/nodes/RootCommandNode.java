package com.cometkaizo.command.nodes;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Root command node of a command
 */
class RootCommandNode extends CommandNode {

    public RootCommandNode(RootCommandNodeBuilder builder) {
        super(builder);
    }


    @Override
    protected boolean accepts(String arg) {
        return true;
    }

    @Override
    protected void executeFunctionality() {

    }
}
