package com.cometkaizo.world.entity;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.event.EventBus;
import com.cometkaizo.game.Game;
import com.cometkaizo.game.GameState;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Renderable;
import com.cometkaizo.world.*;

import java.awt.*;
/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: An Entity is an object which is not bound to the grid as blocks are
 */
public abstract class Entity implements Tickable, Renderable, Resettable {
    protected final Args originalArgs;
    protected final GameState originalGameState;
    protected Vector.ImmutableDouble originalPosition;
    protected Vector.MutableDouble position;
    protected Vector.ImmutableDouble oldPosition;
    protected final GameApp app;
    protected final Game game;
    protected final EventBus eventBus;
    protected Room.Layer layer;
    protected Room room;
    protected String name;
    protected boolean lit;

    /// Creates a new entity
    public Entity(Room.Layer layer, Vector.MutableDouble position, Args args) {
        this.room = layer.room;
        this.layer = layer;
        this.game = room.game;
        this.app = game.getApp();
        this.eventBus = game.getEventBus();
        this.originalArgs = args;
        this.originalGameState = game.getState();
        this.originalPosition = Vector.immutableDouble(position);
        reset();
    }

    /// Reads data in from the world file
    @Override
    public void reset() {
        originalArgs.reset();
        this.position = Vector.mutableDouble(originalPosition);
        updateOldPosition();
        this.name = originalArgs.next();
    }

    /// Saves this entity to the game state
    public void write(GameState state) {

    }

    /// Updates this entity, called every tick
    @Override
    public void tick() {
        updateOldPosition();
    }
    /// Sets this entity to "unlit"
    public void resetLight() {
        lit = false;
    }
    /// Called every tick to update light emission
    public void tickLightEmission() {

    }

    /// Updates the position last tick
    protected void updateOldPosition() {
        this.oldPosition = Vector.immutableDouble(position);
    }

    /// Renders this entity to the screen
    @Override
    public void render(Canvas canvas) {
        var texture = getTexture();
        if (texture == null) return;
        int x = canvas.toScreenX(canvas.lerp(oldPosition.x, getX())) + canvas.scale(getTextureDeltaX());
        int y = canvas.toScreenY(canvas.lerp(oldPosition.y, getY())) + canvas.scale(getTextureDeltaY());
        canvas.renderImage(texture, x, y, getTextureDeltaXFactor(), getTextureDeltaYFactor());
    }
    /// Gets the x translation to be applied to the texture, in unscaled texture pixels
    protected int getTextureDeltaX() {
        return 0;
    }
    /// Gets the y translation to be applied to the texture, in unscaled texture pixels
    protected int getTextureDeltaY() {
        return 0;
    }
    /// Gets the x translation applied to the texture, as a percentage of the width of the image
    protected double getTextureDeltaXFactor() {
        return 0;
    }
    /// Gets the y translation applied to the texture, as a percentage of the height of the image
    protected double getTextureDeltaYFactor() {
        return -1;
    }
    /// Gets the path to the texture
    protected abstract String getTexturePath();
    /// Gets this entity's texture
    protected Image getTexture() {
        String texturePath = getTexturePath();
        if (texturePath == null) return null;
        return Assets.texture("entity/" + texturePath);
    }

    /// Returns whether this entity has a name
    public boolean hasName() {
        return name != null && !name.isBlank();
    }
    /// Returns the name of this entity, possibly null
    public String getName() {
        return name;
    }


    /// Returns whether this entity blocks light from passing through
    public boolean blocksLight() {
        return true;
    }
    /// Updates this entity every tick when it is hit by light from the given direction,
    /// or when it is not hit by light (in which case direction is null)
    public void updateLight(Direction direction) {
        lit = direction != null;
    }

    /// Gets the position of this entity
    public Vector.Double getPosition() {
        return position;
    }

    /// Sets the position of this entity
    public void setPosition(Vector.Double position) {
        setPosition(position.getX(), position.getY());
    }

    /// Sets the position of this entity
    public void setPosition(double x, double y) {
        position.setX(x);
        position.setY(y);
    }

    /// Gets the x position of this entity
    public double getX() {
        return position.x;
    }

    /// Gets the y position of this entity
    public double getY() {
        return position.y;
    }

    /// The y-value at which this entity is compared to other entities to determine which is rendered in front
    public double getRenderY() {
        return position.y;
    }

    /// Gets the x position of this entity last tick
    public double getOldX() {
        return oldPosition.x;
    }

    /// Gets the y position of this entity last tick
    public double getOldY() {
        return oldPosition.y;
    }

    /// Gets the room that this entity is in
    public Room getRoom() {
        return room;
    }

    /// Gets the game that this entity is in
    public Game getGame() {
        return game;
    }

    /// Functional interface for reading in an entity
    @FunctionalInterface
    public interface Reader {
        Entity apply(Room.Layer layer, Vector.MutableDouble pos, Args args);
    }
}
