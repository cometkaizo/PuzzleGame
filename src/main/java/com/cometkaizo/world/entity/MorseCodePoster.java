package com.cometkaizo.world.entity;

import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.overlay.MorseCodePosterOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable morse code poster
 */
public class MorseCodePoster extends Interactable {
    private final MorseCodePosterOverlay overlay = new MorseCodePosterOverlay(app, () -> lit);

    public MorseCodePoster(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 3D));
    }

    @Override
    protected void interact() {
        app.narrate("It appears that the order of the big letters on this poster has been scrambled", overlay);
    }

    @Override
    public boolean blocksLight() {
        return false;
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderDebugBoundingBox(boundingBox, Color.BLUE);
    }

    @Override
    protected void tickBoundingBox() {
        super.tickBoundingBox();
        boundingBox.position.y = position.y; // make the bounding box go above the entity instead of below
    }

    @Override
    protected String getTexturePath() {
        return "morse_code_poster";
    }
}
