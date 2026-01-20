package com.cometkaizo.world.entity;

import com.cometkaizo.screen.Canvas;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import static com.cometkaizo.util.MathUtils.almostEquals;
/**
 * Author: Andy Wang
 * Date Modified: 2026-01-03
 * Description: An entity which can move
 */
public abstract class MovableEntity extends CollidableEntity {
    protected Vector.MutableDouble motion = Vector.mutable(0D, 0D);
    protected boolean collidedHorizontally;
    protected boolean collidedVertically;
    protected Vector.ImmutableDouble oldBoundingBoxPos = Vector.immutable(0D, 0D);

    /// Creates a new movable entity
    public MovableEntity(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    /// Updates this entity, called every tick
    @Override
    public void tick() {
        super.tick();

        move(motion);

        if (collidedVertically) motion.y = 0;
        if (collidedHorizontally) motion.x = 0;
    }

    /// Moves this entity by delta
    protected void move(Vector.Double delta) {
        if (!canCollideWhenMoving()) {
            position.add(delta);
            return;
        }

        double prevX = position.x;
        double prevY = position.y;
        layer.calcAllowedMovement(position, position.addedTo(delta), this, position, canBlip());

        collidedHorizontally = !almostEquals(position.x - prevX, delta.getX());
        collidedVertically = !almostEquals(position.y - prevY, delta.getY());
    }

    /// Returns whether this entity collided this tick
    protected boolean collided() {
        return collidedHorizontally || collidedVertically;
    }

    /// Returns whether this entity collides with solid blocks and entities
    public boolean canCollideWhenMoving() {
        return true;
    }

    /// Returns whether this entity can "blip" around corners
    protected boolean canBlip() {
        return true;
    }

    /// Gets the motion for this entity
    public Vector.Double getMotion() {
        return motion;
    }
    /// Sets this entity's motion
    public void setMotion(Vector.Double motion) {
        setMotion(motion.getX(), motion.getY());
    }
    /// Sets this entity's motion
    public void setMotion(double x, double y) {
        motion.setX(x);
        motion.setY(y);
    }

    /// Updates the position of this entity last tick
    @Override
    protected void updateOldPosition() {
        super.updateOldPosition();
        this.oldBoundingBoxPos = Vector.immutableDouble(boundingBox.position);
    }

    /// Renders this entity to the screen
    @Override
    public void render(Canvas canvas) {
        var texture = getTexture();
        if (texture == null) return;
        int x = canvas.toScreenX(canvas.lerp(oldPosition.x, getX())) + canvas.scale(getTextureDeltaX());
        int y = canvas.toScreenY(canvas.lerp(oldBoundingBoxPos.y, boundingBox.getBottom())) + canvas.scale(getTextureDeltaY());
        canvas.renderImage(texture, x, y, getTextureDeltaXFactor(), getTextureDeltaYFactor());
    }
}
