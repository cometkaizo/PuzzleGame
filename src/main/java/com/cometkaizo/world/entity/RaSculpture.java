package com.cometkaizo.world.entity;

import com.cometkaizo.game.GameState;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.world.*;

import java.awt.*;
/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: Sculpture of Ra
 */
public class RaSculpture extends Interactable {
    private Direction direction;
    private boolean emittingLight;
    /// Creates a new Ra sculpture
    public RaSculpture(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

    /// Reads data in from the world file
    @Override
    public void reset() {
        super.reset();
        direction = originalGameState.raDirection;
        emittingLight = originalGameState.raEmittingLight;
    }

    /// Saves this entity to the game state
    @Override
    public void write(GameState state) {
        super.write(state);
        state.raDirection = direction;
        state.raEmittingLight = emittingLight;
    }

    /// Opens the interaction overlay when the player interacts with this entity
    @Override
    protected void interact() {
        app.narrate(getInteractionMessage(), null);
    }
    private String getInteractionMessage() {
        return "Statue of Ra, the Egyptian God of the Sun." +
                (emittingLight ? "\n\nA powerful beam of light shines from the sculpture." : "");
    }

    /// Called every tick to update light emission
    @Override
    public void tickLightEmission() {
        super.tickLightEmission();
        if (emittingLight) layer.lightUp(lightEmissionPos(), direction, this);
    }
    /// Gets the position at which light is emitted
    private Vector.ImmutableInt lightEmissionPos() {
        if (direction == Direction.DOWN) return Vector.immutableInt(position);
        return Vector.immutableInt(position).addedTo(0, 1);
    }
    /// Turns the light on
    public void turnOnLight() {
        emittingLight = true;
    }

    /// Renders this entity to the screen
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderDebugBoundingBox(boundingBox, Color.BLUE);
    }

    /// Gets the path to the texture
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

    /// Returns whether this entity stops other entities from moving through it
    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    /// Returns whether this entity blocks light from passing through
    @Override
    public boolean blocksLight() {
        return false;
    }

    /// Sets the direction that this statue is facing
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
