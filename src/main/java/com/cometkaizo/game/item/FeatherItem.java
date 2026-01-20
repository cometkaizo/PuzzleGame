package com.cometkaizo.game.item;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: This class represents a feather of truth item
 */
public class FeatherItem extends WeighableItem {
    /// Gets the namespace (id) of this item
    @Override
    protected String getNamespace() {
        return "feather";
    }

    /// Gets the display name of this item
    @Override
    public String getName() {
        return "Feather of Truth";
    }

    /// Gets the weight of this item
    @Override
    public int weight() {
        return 50;
    }
}
