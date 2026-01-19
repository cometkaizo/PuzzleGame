package com.cometkaizo.game.item;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: This class represents a chess key item
 */
public class ChessKeyItem extends Item {
    @Override
    protected String getNamespace() {
        return "chess_key";
    }

    @Override
    public String getName() {
        return "Chess Key";
    }
}
