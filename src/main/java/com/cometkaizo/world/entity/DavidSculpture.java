package com.cometkaizo.world.entity;

import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.overlay.DavidOverlay;
import com.cometkaizo.screen.overlay.NarrationOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;

public class DavidSculpture extends Interactable {
    public static final String OPEN_HEART_MSG = """
            You insert the organ key into the heart and turn it.""";
    private boolean[][] pedestalCombo = {{true, true, false, false, false, true, true}, {false, true, true, true, true, true, false}};
    private boolean chestOpen, heartOpen;

    public DavidSculpture(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(2D, 1D));
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    protected void interact() {
        app.setOverlay(new DavidOverlay(app, chestOpen, heartOpen, pedestalCombo, this::openChest, this::openHeart));
    }

    public void openChest() {
        chestOpen = true;
    }
    public void openHeart() {
        heartOpen = true;
        app.setOverlay(new NarrationOverlay(app, OPEN_HEART_MSG));
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderDebugBoundingBox(boundingBox, Color.BLUE);
    }

    @Override
    protected String getTexturePath() {
        return "sculpture/david";
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }
}
