package com.cometkaizo.game.item;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: This class represents an organ key item
 */
public class OrganKeyItem extends Item {
    /// Gets the namespace (id) of this item
    @Override
    protected String getNamespace() {
        return "organ_key";
    }

    /// Gets the display name of this item
    @Override
    public String getName() {
        return "Organ Key";
    }
}
