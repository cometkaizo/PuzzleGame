package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.LetterOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

public class Letter extends Interactable {
    public Letter(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    protected void interact() {
        app.setOverlay(new LetterOverlay(app));
    }

    @Override
    protected String getTexturePath() {
        return "letter";
    }
}
