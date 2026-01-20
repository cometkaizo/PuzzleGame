package com.cometkaizo.command.nodes;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-19
 * Description: Builder for an empty command node
 */
public class EmptyCommandNodeBuilder extends NoArgCommandNodeBuilder {

    /// Builds this command node
    @Override
    protected EmptyCommandNode build() {
        return new EmptyCommandNode(this);
    }
}
