package com.cometkaizo.world.entity;

import com.cometkaizo.game.GameState;
import com.cometkaizo.world.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: Interactable mirror
 */
public class Mirror extends Interactable {
    private Direction direction;
    /// Creates a new mirror
    public Mirror(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

    /// Reads data in from the world file
    @Override
    public void reset() {
        super.reset();
        direction = originalGameState.mirrorDirection;
    }

    /// Saves this entity to the game state
    @Override
    public void write(GameState state) {
        super.write(state);
        state.mirrorDirection = direction;
    }

    /// Reflects light in the direction this mirror is facing.
    /// Updates this entity every tick when it is hit by light from the given direction,
    /// or when it is not hit by light (in which case direction is null)
    @Override
    public void updateLight(Direction direction) {
        super.updateLight(direction);
        if (lit && this.direction.axis() == Axis.X)
            layer.lightUp(lightEmissionPos(), this.direction, this);
    }
    /// Gets the block position that the light is emitted from
    private Vector.ImmutableInt lightEmissionPos() {
        return Vector.immutableInt(position);
    }

    /// Rotates the mirror when the player interacts with this entity
    @Override
    protected void interact() {
        direction = switch (direction) {
            case UP -> Direction.RIGHT;
            case RIGHT -> Direction.DOWN;
            case DOWN -> Direction.LEFT;
            case LEFT -> Direction.UP;
        };
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

    /// Gets the path to the texture
    @Override
    protected String getTexturePath() {
        return "mirror/" + switch (direction) {
            case UP -> "up";
            case DOWN -> "down";
            case LEFT -> "left";
            case RIGHT -> "right";
        };
    }
}
