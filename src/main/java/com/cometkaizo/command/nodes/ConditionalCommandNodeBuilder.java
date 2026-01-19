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

    public ConditionalCommandNodeBuilder(Supplier<Boolean> condition) {
        this(condition, "CONDITIONAL");
    }

    public ConditionalCommandNodeBuilder(Supplier<Boolean> condition, String name) {
        this.condition = condition;
        this.name = name;
    }

    @Override
    protected ConditionalCommandNode build() {
        return new ConditionalCommandNode(this);
    }
}
