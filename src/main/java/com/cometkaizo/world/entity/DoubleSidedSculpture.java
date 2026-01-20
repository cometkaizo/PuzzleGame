package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.DoubleSidedOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
/**
 * Author: Andy Wang
 * Date Modified: 2026-01-09
 * Description: Interactable double-sided sculpture: Mephistopheles and Margaretta
 */
public class DoubleSidedSculpture extends Interactable {
    /// Creates a new Mephistopheles and Margaretta sculpture
    public DoubleSidedSculpture(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(2D, 1D));
    }

    /// Opens the interaction overlay when the player interacts with this entity
    @Override
    protected void interact() {
        app.setOverlay(new DoubleSidedOverlay(app));
    }

    /// Returns whether this entity stops other entities from moving through it
    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    /// Gets the path to the texture
    @Override
    protected String getTexturePath() {
        return "sculpture/double_sided";
    }
}
