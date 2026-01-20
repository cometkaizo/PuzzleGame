package com.cometkaizo.game.item;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: This class represents a heart item heavier than the feather
 */
public class HeavyHeartItem extends WeighableItem {
    /// Gets the namespace (id) of this item
    @Override
    protected String getNamespace() {
        return "heavy_heart";
    }

    /// Gets the display name of this item
    @Override
    public String getName() {
        return "Heart of War";
    }

    /// Gets the weight of this item
    @Override
    public int weight() {
        return 100;
    }
}
