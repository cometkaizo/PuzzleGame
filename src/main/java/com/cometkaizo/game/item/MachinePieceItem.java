package com.cometkaizo.game.item;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: This class represents a machine piece item
 */
public class MachinePieceItem extends Item {
    /// Gets the namespace (id) of this item
    @Override
    protected String getNamespace() {
        return "machine_piece";
    }

    /// Gets the display name of this item
    @Override
    public String getName() {
        return "Machine Part";
    }
}
