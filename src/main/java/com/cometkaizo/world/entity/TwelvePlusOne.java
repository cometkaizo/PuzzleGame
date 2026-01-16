package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.TwelvePlusOneOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable twelve plus one puzzle
 */
public class TwelvePlusOne extends Interactable {
    private final TwelvePlusOneOverlay overlay = new TwelvePlusOneOverlay(app);

    public TwelvePlusOne(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(2D, 1D));
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    protected void interact() {
        app.setOverlay(overlay);
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    @Override
    protected String getTexturePath() {
        return "twelve_plus_one";
    }

    @Override
    protected int getTextureDeltaX() {
        return -2;
    }
    @Override
    protected int getTextureDeltaY() {
        return 2;
    }
}
