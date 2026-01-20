package com.cometkaizo.world.entity;

import com.cometkaizo.screen.Canvas;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
/**
 * Author: Andy Wang
 * Date Modified: 2026-01-03
 * Description: An entity with a bounding box
 */
public abstract class CollidableEntity extends Entity {
    protected BoundingBox boundingBox;

    /// Creates a new collidable entity
    public CollidableEntity(Room.Layer layer, Vector.MutableDouble position, Args args) {
        // being able to put stuff before super() in java 25 is amazing
        this.boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D)); // default hitbox to 1 block x 1 block
        super(layer, position, args);
    }

    /// Updates this entity, called every tick
    @Override
    public void tick() {
        super.tick();
        tickBoundingBox();
    }

    /**
     * Updates the bounding box every tick according to position
     * @implNote default implementation aligns this entity's bounding box like a block would
      */
    protected void tickBoundingBox() {
        boundingBox.position.x = position.x;
        boundingBox.position.y = position.y - boundingBox.getHeight() + 1;
    }

    /// Reads data in from the world file
    @Override
    public void reset() {
        super.reset();
        if (boundingBox != null) tickBoundingBox();
    }

    /// Sets the position of this entity
    @Override
    public void setPosition(double x, double y) {
        super.setPosition(x, y);
        if (boundingBox != null) tickBoundingBox();
    }

    /// Returns whether this entity is solid to the specified entity
    public boolean isSolid(Entity entity) {
        return false;
    }

    /// Returns the bounding box of this entity
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    /// Returns whether this entity is touching another
    public boolean isTouching(CollidableEntity other) {
        return boundingBox.intersects(other.boundingBox);
    }

    /// Returns whether this entity is within a certain distance from another
    public boolean isTouching(CollidableEntity other, double tolerance) {
        return boundingBox.intersects(other.boundingBox.expanded(tolerance));
    }

    /// Renders this entity to the screen
    @Override
    public void render(Canvas canvas) {
        var texture = getTexture();
        if (texture == null) return;
        int x = canvas.toScreenX(getX()) + canvas.scale(getTextureDeltaX());
        int y = canvas.toScreenY(boundingBox.getBottom()) + canvas.scale(getTextureDeltaY());
        canvas.renderImage(texture, x, y, getTextureDeltaXFactor(), getTextureDeltaYFactor());
    }
}
