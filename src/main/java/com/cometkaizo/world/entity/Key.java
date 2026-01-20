package com.cometkaizo.world.entity;

import com.cometkaizo.game.GameState;
import com.cometkaizo.game.item.EntranceKeyItem;
import com.cometkaizo.game.item.Item;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: Interactable key item
 */
public class Key extends Collectible {
    /// Creates a new key entity
    public Key(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    /// Reads data in from the world file
    @Override
    public void reset() {
        super.reset();
        collected = originalGameState.keyCollected;
    }

    /// Saves this entity to the game state
    @Override
    public void write(GameState state) {
        super.write(state);
        state.keyCollected = collected;
    }

    /// Gets the message for picking up the key
    @Override
    protected String pickupMessage() {
        return "You pick up the key.";
    }

    /// Creates a new key item
    @Override
    protected Item newItem() {
        return new EntranceKeyItem();
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
        return "key";
    }

    /// Gets the y translation applied to the texture, as a percentage of the height of the image
    @Override
    protected double getTextureDeltaYFactor() {
        return -2.5;
    }

    /// The y-value at which this entity is compared to other entities to determine which is rendered in front
    @Override
    public double getRenderY() {
        return position.y + 0.8;
    }
}
