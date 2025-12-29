package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.PaintingOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

public class Painting extends Interactable {
    private String variant, label;
    private int w, h;
    public Painting(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    public void reset() {
        super.reset();
        variant = originalArgs.next("1");
        label = originalArgs.next("Untitled Artwork");

        w = originalArgs.nextInt(1);
        h = originalArgs.nextInt(1);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable((double) w, h));
    }

    @Override
    protected void interact() {
        app.setOverlay(new PaintingOverlay(app, variant, label));
    }

    @Override
    protected String getTexturePath() {
        return "painting/" + variant;
    }

    @Override
    public double getRenderY() {
        if (h == 1) return boundingBox.getTop() - 0.1;
        else return super.getRenderY();
    }
}
