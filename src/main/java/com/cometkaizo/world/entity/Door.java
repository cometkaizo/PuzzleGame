package com.cometkaizo.world.entity;

import com.cometkaizo.game.GameState;
import com.cometkaizo.game.item.ChessKeyItem;
import com.cometkaizo.game.item.EntranceKeyItem;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.overlay.DoorOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;
/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: Interactable door
 */
public class Door extends Interactable {
    private int w, h;
    private boolean open;
    private String roomName;
    /// Creates a new door
    public Door(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    /// Opens the interaction overlay when the player interacts with this entity
    @Override
    protected void interact() {
        if (open) return;
        if (this == game.libraryDoor) app.setOverlay(new DoorOverlay(app, EntranceKeyItem.class, this::open));
        else if (this == game.chessDoor) app.setOverlay(new DoorOverlay(app, ChessKeyItem.class, this::open));
        else app.narrate(getRoomNameMessage() + "It's locked.", null);
    }
    /// Gets the name of the room that this door leads to, possibly null
    private String getRoomNameMessage() {
        return roomName == null ? "" : "The door to the " + roomName + ".\n\n";
    }

    /// Forcibly opens this door
    @Override
    protected void solve() {
        open();
    }
    /// Opens this door
    public void open() {
        if (open) return;
        Assets.sound("door").play();
        open = true;
    }

    /// Reads data in from the world file
    @Override
    public void reset() {
        super.reset();
        open = originalGameState.doorsOpen.getOrDefault(name, false);

        w = originalArgs.nextInt(1);
        h = originalArgs.nextInt(1);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable((double) w, h));

        roomName = originalArgs.next(null);
    }

    /// Returns whether this entity stops other entities from moving through it
    @Override
    public boolean isSolid(Entity entity) {
        return !open;
    }
    /// Returns whether this entity blocks light from passing through
    @Override
    public boolean blocksLight() {
        return !open;
    }

    /// Saves this entity to the game state
    @Override
    public void write(GameState state) {
        super.write(state);
        state.doorsOpen.put(name, open);
    }

    /// Renders this entity to the screen
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderDebugBoundingBox(boundingBox, open ? Color.GREEN : Color.PINK);
    }

    /// Gets the path to the texture
    @Override
    protected String getTexturePath() {
        return "door/" + (open ? "open/" : "closed/") + name;
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

    /// The y-value at which this entity is compared to other entities to determine which is rendered in front
    @Override
    public double getRenderY() {
        return position.y + 0.8;
    }
}
