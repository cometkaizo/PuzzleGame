package com.cometkaizo.world.entity;

import com.cometkaizo.screen.Dialogue;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

public abstract class NPCEntity extends Interactable {
    public NPCEntity(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    protected void interact() {
        if (!game.hasDialogue()) game.setDialogue(newDialogue());
    }

    protected abstract Dialogue newDialogue();
}
