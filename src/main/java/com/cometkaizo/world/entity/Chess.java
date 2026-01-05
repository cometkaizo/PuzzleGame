package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.AutoChessOverlay;
import com.cometkaizo.screen.overlay.ChessOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable chess board
 */
public class Chess extends Interactable {
    private String board;
    private String variant;
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
        variant = originalArgs.next("regular");
    }

    @Override
    protected void interact() {
        if ("auto".equals(variant)) app.setOverlay(new AutoChessOverlay(app, board));
        else app.setOverlay(new ChessOverlay(app, board));
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    @Override
    protected String getTexturePath() {
        return "chess";
    }
    @Override
    protected int getTextureDeltaX() {
        return -2;
    }
    @Override
    protected int getTextureDeltaY() {
        return 2;
    }
}
