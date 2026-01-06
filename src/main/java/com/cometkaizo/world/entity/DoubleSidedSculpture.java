package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.DoubleSidedOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable double-sided sculpture
 */
public class DoubleSidedSculpture extends Interactable {
    private boolean mirrorRemoved;

    public DoubleSidedSculpture(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(2D, 2D));
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    protected void interact() {
        app.setOverlay(new DoubleSidedOverlay(app, mirrorRemoved));
    }

    @Override
    protected String getTexturePath() {
        return "sculpture/double_sided";
    }
}
