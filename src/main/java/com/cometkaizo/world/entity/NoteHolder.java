package com.cometkaizo.world.entity;

import com.cometkaizo.game.GameState;
import com.cometkaizo.game.item.NoteItem;
import com.cometkaizo.screen.overlay.NoteHolderOverlay;
import com.cometkaizo.world.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-08
 * Description: Interactable note-holder for light to shine through
 */
public class NoteHolder extends Interactable {
    private NoteItem[] notes;

    /// Creates a new note holder
    public NoteHolder(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

    /// Reads data in from the world file
    @Override
    public void reset() {
        super.reset();
        notes = originalGameState.noteHolderNotes;
    }

    /// Saves this entity to the game state
    @Override
    public void write(GameState state) {
        super.write(state);
        state.noteHolderNotes = notes;
    }

    /// Opens the interaction overlay when the player interacts with this entity
    @Override
    protected void interact() {
        app.setOverlay(new NoteHolderOverlay(app, lit, notes));
    }

    /// Returns whether this entity stops other entities from moving through it
    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    /// Gets the path to the texture
    @Override
    protected String getTexturePath() {
        return "note_holder";
    }

    /// The y-value at which this entity is compared to other entities to determine which is rendered in front
    @Override
    public double getRenderY() {
        return super.getRenderY() + 0.1;
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
}
