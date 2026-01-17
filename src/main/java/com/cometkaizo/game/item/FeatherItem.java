package com.cometkaizo.game.item;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: This class represents a feather of truth item
 */
public class FeatherItem extends WeighableItem {
    @Override
    protected String getNamespace() {
        return "feather";
    }

    @Override
    public String getName() {
        return "Feather of Truth";
    }

    @Override
    public int weight() {
        return 50;
    }
}
