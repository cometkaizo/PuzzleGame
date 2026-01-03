package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.ChessOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

public class Chess extends Interactable {
    // 8/4N2n/1B5k/1KQ5/6r1/8/8/1R6 w - - 0 1
    //
    private String board;
    public Chess(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

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
    }

    @Override
    protected void interact() {
        app.setOverlay(new ChessOverlay(app, board));
    }

    @Override
    protected String getTexturePath() {
        return "chess";
    }
}
