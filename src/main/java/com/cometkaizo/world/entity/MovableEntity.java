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
    protected Vector.MutableDouble motion = Vector.mutable(0D, 0D), groundMotion = Vector.mutable(0D, 0D);
    protected boolean collidedHorizontally;
    protected boolean collidedVertically;
    protected Vector.ImmutableDouble oldBoundingBoxPos = Vector.immutable(0D, 0D);

    public MovableEntity(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    public void tick() {
        super.tick();

        move(motion.addedTo(groundMotion));
        groundMotion.setX(0D);
        groundMotion.setY(0D);

        if (collidedVertically) motion.y = 0;
        if (collidedHorizontally) motion.x = 0;
    }

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

    protected boolean collided() {
        return collidedHorizontally || collidedVertically;
    }

    public boolean canCollideWhenMoving() {
        return true;
    }

    protected boolean canBlip() {
        return true;
    }

    public Vector.Double getMotion() {
        return motion;
    }
    public void setMotion(Vector.Double motion) {
        setMotion(motion.getX(), motion.getY());
    }
    public void setMotion(double x, double y) {
        motion.setX(x);
        motion.setY(y);
    }
    public Vector.Double getGroundMotion() {
        return groundMotion;
    }
    public void setGroundMotion(Vector.Double motion) {
        setGroundMotion(motion.getX(), motion.getY());
    }
    public void setGroundMotion(double x, double y) {
        groundMotion.setX(x);
        groundMotion.setY(y);
    }

    @Override
    protected void updateOldPosition() {
        super.updateOldPosition();
        this.oldBoundingBoxPos = Vector.immutableDouble(boundingBox.position);
    }

    @Override
    public void render(Canvas canvas) {
        var texture = getTexture();
        if (texture == null) return;
        int x = canvas.toScreenX(canvas.lerp(oldPosition.x, getX())) + canvas.scale(getTextureDeltaX());
        int y = canvas.toScreenY(canvas.lerp(oldBoundingBoxPos.y, boundingBox.getBottom())) + canvas.scale(getTextureDeltaY());
        canvas.renderImage(texture, x, y, getTextureDeltaXFactor(), getTextureDeltaYFactor());
    }
}
