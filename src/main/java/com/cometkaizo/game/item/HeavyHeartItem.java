package com.cometkaizo.game.item;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: This class represents a heart item heavier than the feather
 */
public class HeavyHeartItem extends WeighableItem {
    @Override
    protected String getNamespace() {
        return "heavy_heart";
    }

    @Override
    public String getName() {
        return "Heart of War";
    }

    @Override
    public int weight() {
        return 100;
    }
}
