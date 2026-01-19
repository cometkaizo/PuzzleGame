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
    public Mirror(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

    @Override
    public void reset() {
        super.reset();
        direction = originalGameState.mirrorDirection;
    }

    @Override
    public void write(GameState state) {
        super.write(state);
        state.mirrorDirection = direction;
    }

    @Override
    public void tickLightEmission() {
        super.tickLightEmission();
    }

    @Override
    public void updateLight(Direction direction) {
        super.updateLight(direction);
        if (lit && this.direction.axis() == Axis.X)
            layer.lightUp(lightEmissionPos(), this.direction, this);
    }

    private Vector.ImmutableInt lightEmissionPos() {
        return Vector.immutableInt(position);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void interact() {
        direction = switch (direction) {
            case UP -> Direction.RIGHT;
            case RIGHT -> Direction.DOWN;
            case DOWN -> Direction.LEFT;
            case LEFT -> Direction.UP;
        };
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
    protected String getTexturePath() {
        return "mirror/" + switch (direction) {
            case UP -> "up";
            case DOWN -> "down";
            case LEFT -> "left";
            case RIGHT -> "right";
        };
    }
}
