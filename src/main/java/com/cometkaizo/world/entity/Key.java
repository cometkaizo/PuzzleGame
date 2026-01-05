package com.cometkaizo.world.entity;

import com.cometkaizo.game.item.EntranceKeyItem;
import com.cometkaizo.game.item.Item;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

public class Key extends Collectible {
    public Key(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    protected Item newItem() {
        return new EntranceKeyItem();
    }

    @Override
    protected String getTexturePath() {
        return "key";
    }
}
