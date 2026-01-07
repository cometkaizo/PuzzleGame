package com.cometkaizo.world.entity;

import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable cupid and psyche sculpture
 */
public class CupidAndPsycheSculpture extends Interactable {
    public CupidAndPsycheSculpture(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(2D, 1D));
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    protected void interact() {

    }

    @Override
    protected String getTexturePath() {
        return "sculpture/cupid_and_psyche";
    }
}
