package com.cometkaizo.command.nodes;

import java.util.function.Supplier;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-19
 * Description: Builder for a conditional command node
 */
public class ConditionalCommandNodeBuilder extends NoArgCommandNodeBuilder {

    protected final Supplier<Boolean> condition;
    protected final String name;

    /// Creates a new command node builder
    public ConditionalCommandNodeBuilder(Supplier<Boolean> condition, String name) {
        this.condition = condition;
        this.name = name;
    }

    /// Builds this command node
    @Override
    protected ConditionalCommandNode build() {
        return new ConditionalCommandNode(this);
    }
}
