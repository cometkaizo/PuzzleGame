package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.HopeDiamondOverlay;
import com.cometkaizo.world.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-08
 * Description: Interactable hope diamond
 */
public class HopeDiamond extends Interactable {
    public HopeDiamond(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    protected void interact() {
        app.setOverlay(new HopeDiamondOverlay(app, lit));
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    @Override
    protected String getTexturePath() {
        return "hope_diamond";
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
