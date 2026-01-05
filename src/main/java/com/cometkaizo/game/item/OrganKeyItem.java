package com.cometkaizo.game.item;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: This class represents an organ key item
 */
public class OrganKeyItem extends Item {
    @Override
    protected String getTexturePath() {
        return "organ_key";
    }

    @Override
    public String getName() {
        return "Organ Key";
    }
}
