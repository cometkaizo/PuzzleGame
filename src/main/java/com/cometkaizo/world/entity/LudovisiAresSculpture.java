package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.LudovisiAresOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable ludovisi ares sculpture
 */
public class LudovisiAresSculpture extends Interactable {
    private boolean chestOpen, heartOpen;
    public LudovisiAresSculpture(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(2D, 1D));
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    protected void interact() {
        app.setOverlay(new LudovisiAresOverlay(app, chestOpen, heartOpen, () -> chestOpen = true, () -> heartOpen = true));
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    @Override
    protected String getTexturePath() {
        return "sculpture/ares";
    }
}
