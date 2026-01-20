package com.cometkaizo.world.entity;

import com.cometkaizo.game.GameState;
import com.cometkaizo.game.item.OrganKeyItem;
import com.cometkaizo.screen.overlay.NarrationOverlay;
import com.cometkaizo.screen.overlay.OrganOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: Interactable organ
 */
public class Organ extends Interactable {
    public static final String KEY_FALL_OUT_MSG = """
            One of the keys on the organ shakes loose. You take it as it falls out.""";
    private long keyFallOutTick = -1;
    private boolean keyFallenOut;

    /// Creates a new organ instrument
    public Organ(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(2D, 2D));
    }

    /// Reads data in from the world file
    @Override
    public void reset() {
        super.reset();
        keyFallenOut = originalGameState.organKeyFallenOut;
    }

    /// Saves this entity to the game state
    @Override
    public void write(GameState state) {
        super.write(state);
        state.organKeyFallenOut = keyFallenOut;
    }

    /// Opens the interaction overlay when the player interacts with this entity
    @Override
    protected void interact() {
        app.setOverlay(new OrganOverlay(app, keyFallenOut, this::startKeyFallOut));
    }

    private void startKeyFallOut() {
        if (keyFallenOut) return;
        keyFallOutTick = game.tick + 20;
    }

    /// Updates this entity, called every tick
    @Override
    public void tick() {
        super.tick();
        if (game.tick == keyFallOutTick) {
            makeKeyFallOut();
        }
    }

    /// Makes the organ key fall out
    private void makeKeyFallOut() {
        if (app.getOverlay() instanceof OrganOverlay o) o.keyFallenOut = true;
        app.setOverlay(new NarrationOverlay(app, KEY_FALL_OUT_MSG, app.getOverlay()));
        game.getInventory().add(new OrganKeyItem());
        keyFallenOut = true;
    }

    /// Returns whether this entity stops other entities from moving through it
    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    /// Gets the path to the texture
    @Override
    protected String getTexturePath() {
        return "organ";
    }
}
