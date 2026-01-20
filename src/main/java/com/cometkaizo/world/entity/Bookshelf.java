package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.BookOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-09
 * Description: Interactable bookshelf with a single readable book in it
 */
public class Bookshelf extends Interactable {
    private int variant;

    /// Creates a bookshelf
    public Bookshelf(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(4D, 1D));
    }

    /// Reads data in from the world file
    @Override
    public void reset() {
        super.reset();
        variant = originalArgs.nextInt(1) - 1;
    }

    /// Opens the interaction overlay when the player interacts with this entity
    @Override
    protected void interact() {
        app.setOverlay(new BookOverlay(app, variant));
    }

    /// Gets the path to the texture
    @Override
    protected String getTexturePath() {
        return "bookshelf";
    }

    /// Returns whether this entity stops other entities from moving through it
    @Override
    public boolean isSolid(Entity entity) {
        return true;
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
