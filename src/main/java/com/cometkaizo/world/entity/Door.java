package com.cometkaizo.world.entity;

import com.cometkaizo.game.item.EntranceKeyItem;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.overlay.DoorOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;

public class Door extends Interactable {
    private int w, h;
    private boolean open;
    public Door(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    protected void interact() {
        if (open) return;
        if (this == game.libraryDoor) app.setOverlay(new DoorOverlay(app, EntranceKeyItem.class, this::open));
        else open();
    }
    public void open() {
        open = true;
    }

    @Override
    public void reset() {
        super.reset();
        open = false;
        w = originalArgs.nextInt(1);
        h = originalArgs.nextInt(1);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable((double) w, h));
    }

    @Override
    public boolean isSolid(Entity entity) {
        return !open;
    }
    @Override
    public boolean blocksLight() {
        return !open;
    }

    @Override
    public void render(Canvas canvas) {
        canvas.renderDebugBoundingBox(boundingBox, open ? Color.GREEN : Color.PINK);
    }

    @Override
    protected String getTexturePath() {
        return "door";
    }
}
