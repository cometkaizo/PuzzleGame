package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.TwelvePlusOneOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-16
 * Description: Interactable twelve plus one puzzle
 */
public class TwelvePlusOne extends Interactable {
    private final TwelvePlusOneOverlay overlay = new TwelvePlusOneOverlay(app);

    /// Creates a new "12 + 1" puzzle
    public TwelvePlusOne(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(2D, 1D));
    }

    /// Opens the interaction overlay when the player interacts with this entity
    @Override
    protected void interact() {
        app.setOverlay(overlay);
    }

    /// Returns whether this entity stops other entities from moving through it
    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    /// Gets the path to the texture
    @Override
    protected String getTexturePath() {
        return "twelve_plus_one";
    }

    /// Gets the x translation to be applied to the texture, in unscaled texture pixels
    @Override
    protected int getTextureDeltaX() {
        return -2;
    }
    /// Gets the y translation to be applied to the texture, in unscaled texture pixels
    @Override
    protected int getTextureDeltaY() {
        return 2;
    }
}
