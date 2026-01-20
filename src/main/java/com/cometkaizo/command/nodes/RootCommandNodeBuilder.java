package com.cometkaizo.command.nodes;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Builder for a root command node
 */
public class RootCommandNodeBuilder extends CommandNodeBuilder {

    /// Creates a new root command node builder
    public RootCommandNodeBuilder() {
        // root command nodes always have a level of -1
        level = -1;
    }

    /// Builds this command node
    @Override
    protected RootCommandNode build() {
        return new RootCommandNode(this);
    }
}
