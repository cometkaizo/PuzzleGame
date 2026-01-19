package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.RosettaStoneOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
/**
 * Author: Andy Wang
 * Date Modified: 2026-01-15
 * Description: Interactable rosetta stone
 */
public class RosettaStone extends Interactable {
    public RosettaStone(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

    @Override
    protected void interact() {
        app.setOverlay(new RosettaStoneOverlay(app));
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    @Override
    protected String getTexturePath() {
        return "rosetta_stone";
    }
}
