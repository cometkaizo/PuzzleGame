package com.cometkaizo.world.entity;

import com.cometkaizo.game.GameState;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.overlay.MorseCodePosterOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: Interactable morse code poster
 */
public class MorseCodePoster extends Interactable {
    private int[] noteIds;
    private MorseCodePosterOverlay overlay;

    /// Creates a new morse code poster
    public MorseCodePoster(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 3D));
    }

    /// Reads data in from the world file
    @Override
    public void reset() {
        super.reset();
        noteIds = originalGameState.morseCodePosterNotes;
        overlay = new MorseCodePosterOverlay(app, () -> lit, noteIds);
    }

    /// Saves this entity to the game state
    @Override
    public void write(GameState state) {
        super.write(state);
        originalGameState.morseCodePosterNotes = overlay.noteIds();
    }

    /// Opens the interaction overlay when the player interacts with this entity
    @Override
    protected void interact() {
        app.narrate("It appears that the order of the big letters on this poster has been scrambled", overlay);
    }

    /// Returns whether this entity blocks light from passing through
    @Override
    public boolean blocksLight() {
        return false;
    }

    /// Renders this entity to the screen
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderDebugBoundingBox(boundingBox, Color.BLUE);
    }

    /// Updates the bounding box to the correct position every tick
    @Override
    protected void tickBoundingBox() {
        super.tickBoundingBox();
        boundingBox.position.y = position.y; // make the bounding box go above the entity instead of below
    }

    /// Gets the path to the texture
    @Override
    protected String getTexturePath() {
        return "morse_code_poster";
    }
}
