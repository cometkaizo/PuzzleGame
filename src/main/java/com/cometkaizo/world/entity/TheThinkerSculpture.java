package com.cometkaizo.world.entity;

import com.cometkaizo.game.GameState;
import com.cometkaizo.screen.overlay.TheThinkerOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: Interactable "The Thinker" sculpture
 */
public class TheThinkerSculpture extends Interactable {
    private boolean brainSolved;
    private TheThinkerOverlay overlay;

    /// Creates a new thinker sculpture
    public TheThinkerSculpture(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(2D, 1D));
    }

    /// Reads data in from the world file
    @Override
    public void reset() {
        super.reset();
        brainSolved = originalGameState.thinkerBrainSolved;
        overlay = new TheThinkerOverlay(app, brainSolved, () -> brainSolved = true);
    }

    /// Saves this entity to the game state
    @Override
    public void write(GameState state) {
        super.write(state);
        state.thinkerBrainSolved = brainSolved;
    }

    /// Opens the interaction overlay when the player interacts with this entity
    @Override
    protected void interact() {
        app.setOverlay(overlay);
    }

    /// Returns whether this entity stops other entities from moving through it
    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    /// Gets the path to the texture
    @Override
    protected String getTexturePath() {
        return "sculpture/thinker";
    }
}
