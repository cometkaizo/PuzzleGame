package com.cometkaizo.game.item;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: This class represents an entrance key item
 */
public class EntranceKeyItem extends Item {
    @Override
    protected String getTexturePathImpl() {
        return "entrance_key";
    }

    @Override
    public String getName() {
        return "Entrance Key";
    }
}
