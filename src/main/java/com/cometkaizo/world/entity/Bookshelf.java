package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.BookOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable bookshelf with a single readable book in it
 */
public class Bookshelf extends Interactable {
    private int variant;

    public Bookshelf(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(4D, 1D));
    }

    @Override
    public void reset() {
        super.reset();
        variant = originalArgs.nextInt(1) - 1;
    }

    @Override
    protected void interact() {
        app.setOverlay(new BookOverlay(app, variant));
    }

    @Override
    protected String getTexturePath() {
        return "bookshelf";
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
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
