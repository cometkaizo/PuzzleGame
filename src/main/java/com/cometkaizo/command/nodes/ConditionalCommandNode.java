package com.cometkaizo.command.nodes;

import java.util.function.Supplier;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Command node that succeeds if its condition is met
 */
class ConditionalCommandNode extends NoArgCommandNode {

    protected final String name;
    protected final Supplier<Boolean> condition;

    public ConditionalCommandNode(ConditionalCommandNodeBuilder builder) {
        super(builder);
        this.condition = builder.condition;
        this.name = builder.name;
    }

    @Override
    protected boolean accepts() {
        return condition.get();
    }

    @Override
    public String toString() {
        return "ConditionalCommandNode{" +
                "name='" + name + '\'' +
                ", condition=" + condition +
                '}';
    }

    @Override
    public String toPrettyString() {
        return name;
    }
}
