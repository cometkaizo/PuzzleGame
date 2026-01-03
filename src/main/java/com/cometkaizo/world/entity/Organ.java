package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.NarrationOverlay;
import com.cometkaizo.screen.overlay.OrganOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

public class Organ extends Interactable {
    public static final String KEY_FALL_OUT_MSG = """
            One of the keys on the organ shakes loose. You take it as it falls out.""";
    private long keyFallOutTick = -1;
    private boolean keyFallenOut;

    public Organ(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(2D, 2D));
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    protected void interact() {
        app.setOverlay(new OrganOverlay(app, keyFallenOut, this::makeKeyFallOut));
    }

    private void makeKeyFallOut() {
        if (keyFallenOut) return;
        keyFallOutTick = game.tick + 20;
    }

    @Override
    public void tick() {
        super.tick();
        if (game.tick == keyFallOutTick) {
            if (app.getOverlay() instanceof OrganOverlay o) o.keyFallenOut = true;
            app.setOverlay(new NarrationOverlay(app, KEY_FALL_OUT_MSG, app.getOverlay()));
            keyFallenOut = true;
        }
    }

    @Override
    protected String getTexturePath() {
        return "organ";
    }
}
