package com.cometkaizo.game.item;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: This class represents a chess key item
 */
public class ChessKeyItem extends Item {
    /// Gets the namespace (id) of this item
    @Override
    protected String getNamespace() {
        return "chess_key";
    }

    /// Gets the display name of this item
    @Override
    public String getName() {
        return "Chess Key";
    }
}
