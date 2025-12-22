package com.cometkaizo.world.entity;

import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

public class Sign extends Interactable {
    public Sign(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    protected void interact() {
        // todo: set overlay to letter overlay
    }

    @Override
    protected String getTexturePath() {
        return "sign";
    }
}
