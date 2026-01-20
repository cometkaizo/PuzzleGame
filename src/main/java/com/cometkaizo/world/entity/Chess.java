package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.AutoChessOverlay;
import com.cometkaizo.screen.overlay.ChessOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
/**
 * Author: Andy Wang
 * Date Modified: 2026-01-04
 * Description: Interactable chess board
 */
public class Chess extends Interactable {
    private String board;
    private String variant;
    /// Creates a new chess board
    public Chess(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

    /// Reads data in from the world file
    @Override
    public void reset() {
        super.reset();
        String emptyRow = "        ";
        board = originalArgs.next(emptyRow) +"\n"+
                originalArgs.next(emptyRow) +"\n"+
                originalArgs.next(emptyRow) +"\n"+
                originalArgs.next(emptyRow) +"\n"+
                originalArgs.next(emptyRow) +"\n"+
                originalArgs.next(emptyRow) +"\n"+
                originalArgs.next(emptyRow) +"\n"+
                originalArgs.next(emptyRow);
        variant = originalArgs.next("regular");
    }

    /// Opens the interaction overlay when the player interacts with this entity
    @Override
    protected void interact() {
        if ("auto".equals(variant)) app.setOverlay(new AutoChessOverlay(app, board));
        else app.setOverlay(new ChessOverlay(app, board));
    }

    /// Returns whether this entity stops other entities from moving through it
    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    /// Gets the path to the texture
    @Override
    protected String getTexturePath() {
        return "chess";
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
