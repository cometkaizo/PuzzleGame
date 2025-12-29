package com.cometkaizo.world.entity;

import com.cometkaizo.screen.Canvas;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import static com.cometkaizo.util.MathUtils.almostEquals;

public abstract class CollidableEntity extends MovableEntity {
    protected BoundingBox boundingBox;
    protected boolean collidedHorizontally;
    protected boolean collidedVertically;
    protected Vector.ImmutableDouble oldBoundingBoxPos = Vector.immutable(0D, 0D);

    public CollidableEntity(Room.Layer layer, Vector.MutableDouble position, Args args) {
        // being able to put stuff before super() in java 25 is amazing
        this.boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D)); // default hitbox to 1 block x 1 block
        super(layer, position, args);
    }

    @Override
    public void tick() {
        super.tick();
        tickBoundingBox();

        if (collidedVertically) motion.y = 0;
        if (collidedHorizontally) motion.x = 0;
    }

    @Override
    public void move(Vector.Double delta) {
        if (!canCollideWhenMoving()) {
            super.move(delta);
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

    /**
     * Updates the bounding box every tick according to position
     * @implNote default implementation aligns this entity's bounding box like a block would
      */
    protected void tickBoundingBox() {
        boundingBox.position.x = position.x;
        boundingBox.position.y = position.y - boundingBox.getHeight() + 1;
    }

    @Override
    public void reset() {
        super.reset();
        if (boundingBox != null) tickBoundingBox();
    }

    @Override
    public void setPosition(double x, double y) {
        super.setPosition(x, y);
        if (boundingBox != null) tickBoundingBox();
    }

    /// Returns whether this entity is solid to the specified entity
    public boolean isSolid(Entity entity) {
        return false;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public boolean isTouching(CollidableEntity other) {
        return boundingBox.intersects(other.boundingBox);
    }

    public boolean isTouching(CollidableEntity other, double tolerance) {
        return boundingBox.intersects(other.boundingBox.expanded(tolerance));
    }

    public boolean isFloating() {
        return !room.ground.containsSolid(boundingBox, this);
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
