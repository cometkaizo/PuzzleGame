package com.cometkaizo.game.item;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: This class represents a heart item lighter than the feather
 */
public class LightHeartItem extends WeighableItem {
    @Override
    protected String getNamespace() {
        return "light_heart";
    }

    @Override
    public String getName() {
        return "Heart of Love";
    }

    @Override
    public int weight() {
        return 0;
    }
}
