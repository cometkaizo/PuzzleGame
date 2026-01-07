package com.cometkaizo.world.entity;

import com.cometkaizo.game.item.WeighableItem;
import com.cometkaizo.screen.overlay.AnubisOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable anubis sculpture
 */
public class AnubisSculpture extends Interactable {
    private WeighableItem weighed;
    public AnubisSculpture(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    protected void interact() {
        app.setOverlay(new AnubisOverlay(app, weighed, this::onWeigh));
    }

    private void onWeigh(WeighableItem item, AnubisOverlay.WeighResult weighResult) {
        this.weighed = item;
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    @Override
    protected String getTexturePath() {
        return "sculpture/anubis";
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
