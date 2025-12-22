package com.cometkaizo.world.entity;

import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

public class Painting extends Interactable {
    private String variant;
    private int w, h;
    public Painting(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    public void reset() {
        super.reset();
        variant = originalArgs.next("1");

        // I could separate direction and length into two arguments but this looks cleaner on the Google sheet
        String sizeCode = originalArgs.next("r1");
        if (sizeCode.charAt(0) == 'r') {
            w = Integer.parseInt(sizeCode.substring(1));
            h = 1;
        } else {
            w = 1;
            h = Integer.parseInt(sizeCode.substring(1));
        }

        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable((double) w, h));
    }

    @Override
    protected void interact() {

    }

    @Override
    protected String getTexturePath() {
        return "painting/" + variant;
    }
}
