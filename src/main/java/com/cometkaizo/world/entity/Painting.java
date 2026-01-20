package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.PaintingOverlay;
import com.cometkaizo.world.*;
import com.cometkaizo.world.block.WallBlock;
/**
 * Author: Andy Wang
 * Date Modified: 2026-01-04
 * Description: Interactable painting
 */
public class Painting extends Interactable {
    private String variant, label;
    private int w, h;

    /// Creates a new painting
    public Painting(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    /// Reads data in from the world file
    @Override
    public void reset() {
        super.reset();
        variant = originalArgs.next("1");
        label = originalArgs.next("Untitled Artwork");

        w = originalArgs.nextInt(1);
        h = originalArgs.nextInt(1);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable((double) w, h));
    }

    /// Opens the interaction overlay when the player interacts with this entity
    @Override
    protected void interact() {
        app.setOverlay(new PaintingOverlay(app, lit, variant, label));
    }

    /// Returns whether this entity can be interacted
    @Override
    protected boolean canBeInteracted() {
        // expand interaction hitbox up by 0.8 blocks so that paintings with walls below them can be interacted from farther up
        return room.player.canInteract() && boundingBox.expanded(0.8, 0, 0, 0).expanded(0.1).intersects(room.player.boundingBox);
    }

    /// Gets the path to the texture
    @Override
    protected String getTexturePath() {
        return "painting/" + variant;
    }

    /// The y-value at which this entity is compared to other entities to determine which is rendered in front
    @Override
    public double getRenderY() {
        if (w != 1) {
            // prevent the player from rendering below this painting if it's up against a wall
            boolean hasWallAbove = layer.isBlockType((int) getX(), (int) getY() + 1, WallBlock.class);
            if (hasWallAbove) return boundingBox.getTop() - 0.1;
            boolean hasWallBelow = layer.isBlockType((int) getX(), (int) getY() - 1, WallBlock.class);
            if (hasWallBelow) return boundingBox.getBottom() - 0.1;
        }
        return super.getRenderY();
    }

    /// Returns whether this entity blocks light from passing through
    @Override
    public boolean blocksLight() {
        return false;
    }
}
