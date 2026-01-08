package com.cometkaizo.world.entity;

import com.cometkaizo.game.item.NoteItem;
import com.cometkaizo.screen.overlay.NoteHolderOverlay;
import com.cometkaizo.world.*;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable note-holder for light to shine through
 */
public class NoteHolder extends Interactable {
    private boolean lit;
    private NoteItem[] notes = new NoteItem[4];

    public NoteHolder(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    protected void interact() {
        app.setOverlay(new NoteHolderOverlay(app, lit, notes));
    }

    @Override
    public void updateLight(Direction direction) {
        super.updateLight(direction);
        lit = direction != null;
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    @Override
    protected String getTexturePath() {
        return "note_holder";
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
