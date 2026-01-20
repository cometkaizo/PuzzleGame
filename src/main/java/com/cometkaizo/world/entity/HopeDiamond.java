package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.HopeDiamondOverlay;
import com.cometkaizo.world.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-08
 * Description: Interactable hope diamond
 */
public class HopeDiamond extends Interactable {
    /// Creates a new hope diamond
    public HopeDiamond(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

    /// Opens the interaction overlay when the player interacts with this entity
    @Override
    protected void interact() {
        app.setOverlay(new HopeDiamondOverlay(app, lit));
    }

    /// Returns whether this entity stops other entities from moving through it
    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    /// Gets the path to the texture
    @Override
    protected String getTexturePath() {
        return "hope_diamond";
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
