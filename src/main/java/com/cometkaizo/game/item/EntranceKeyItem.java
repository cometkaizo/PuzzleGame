package com.cometkaizo.game.item;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: This class represents an entrance key item
 */
public class EntranceKeyItem extends Item {
    @Override
    protected String getNamespace() {
        return "entrance_key";
    }

    @Override
    public String getName() {
        return "Entrance Key";
    }
}
