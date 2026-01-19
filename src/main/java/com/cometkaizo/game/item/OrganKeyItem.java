package com.cometkaizo.game.item;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: This class represents an organ key item
 */
public class OrganKeyItem extends Item {
    @Override
    protected String getNamespace() {
        return "organ_key";
    }

    @Override
    public String getName() {
        return "Organ Key";
    }
}
