package com.cometkaizo.world.entity;

import com.cometkaizo.game.GameState;
import com.cometkaizo.screen.overlay.HermesOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: Interactable hermes sculpture
 */
public class HermesSculpture extends Interactable {
    private boolean open;

    /// Creates a new Hermes sculpture
    public HermesSculpture(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

    /// Reads data in from the world file
    @Override
    public void reset() {
        super.reset();
        open = originalGameState.hermesSolved;
    }

    /// Saves this entity to the game state
    @Override
    public void write(GameState state) {
        super.write(state);
        state.hermesSolved = open;
    }

    /// Opens the interaction overlay when the player interacts with this entity
    @Override
    protected void interact() {
        app.setOverlay(new HermesOverlay(app, open, () -> open = true));
    }

    /// Returns whether this entity stops other entities from moving through it
    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    /// Gets the path to the texture
    @Override
    protected String getTexturePath() {
        return "sculpture/hermes";
    }

    /// Gets the x translation to be applied to the texture, in unscaled texture pixels
    @Override
    protected int getTextureDeltaX() {
        return super.getTextureDeltaX() - 16;
    }
}
