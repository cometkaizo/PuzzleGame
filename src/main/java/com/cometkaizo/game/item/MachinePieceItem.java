package com.cometkaizo.game.item;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: This class represents a machine piece item
 */
public class MachinePieceItem extends Item {
    @Override
    protected String getNamespace() {
        return "machine_piece";
    }

    @Override
    public String getName() {
        return "Machine Part";
    }
}
