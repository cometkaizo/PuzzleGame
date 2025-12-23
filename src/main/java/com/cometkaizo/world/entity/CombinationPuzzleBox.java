package com.cometkaizo.world.entity;

import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.overlay.CombinationLockOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;

public class CombinationPuzzleBox extends Interactable {
    private String correctCombination;
    private String[] digitOptions;
    private int w, h;
    public CombinationPuzzleBox(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    protected void interact() {
        app.setOverlay(new CombinationLockOverlay(app, correctCombination, digitOptions));
    }

    @Override
    public void reset() {
        super.reset();
        w = originalArgs.nextInt(1);
        h = originalArgs.nextInt(1);
        correctCombination = originalArgs.next("");
        digitOptions = originalArgs.next("").split(" ");
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable((double) w, h));
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    @Override
    public void render(Canvas canvas) {
        canvas.renderDebugBoundingBox(boundingBox, Color.DARK_GRAY);
    }

    @Override
    protected String getTexturePath() {
        return "combination_puzzle_box/" + w + "x" + h;
    }
}
