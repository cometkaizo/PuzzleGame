package com.cometkaizo.world.entity;

import com.cometkaizo.screen.Canvas;
import com.cometkaizo.world.*;

import java.awt.*;
/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Sculpture of Ra
 */
public class RaSculpture extends Interactable {
    private Direction direction = Direction.UP;
    private boolean emittingLight = false;
    public RaSculpture(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    protected void interact() {
        app.narrate(getInteractionMessage(), null);
    }
    private String getInteractionMessage() {
        return "Statue of Ra, the Egyptian God of the Sun." +
                (emittingLight ? "\n\nA powerful beam of light shines from the sculpture." : "");
    }

    @Override
    public void tickLightEmission() {
        super.tickLightEmission();
        if (emittingLight) layer.lightUp(lightEmissionPos(), direction, this);
    }
    private Vector.ImmutableInt lightEmissionPos() {
        if (direction == Direction.DOWN) return Vector.immutableInt(position);
        return Vector.immutableInt(position).addedTo(0, 1);
    }
    public void turnOnLight() {
        emittingLight = true;
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderDebugBoundingBox(boundingBox, Color.BLUE);
    }

    @Override
    protected String getTexturePath() {
        if (!emittingLight) return "sculpture/ra/off";
        return "sculpture/ra/" + switch (direction) {
            case UP -> "up";
            case DOWN -> "down";
            case LEFT -> "left";
            case RIGHT -> "right";
        };
    }

    @Override
    protected int getTextureDeltaX() {
        return -2;
    }
    @Override
    protected int getTextureDeltaY() {
        return 2;
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    @Override
    public boolean blocksLight() {
        return false;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
