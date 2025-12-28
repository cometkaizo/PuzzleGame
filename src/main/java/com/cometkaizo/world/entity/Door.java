package com.cometkaizo.world.entity;

import com.cometkaizo.screen.Canvas;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;

public class Door extends Interactable {
    private String keyName;
    private int w, h;
    private boolean open;
    public Door(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    protected void interact() {
        // todo: check if player has the key with the matching name in their inventory
        if (!keyName.isEmpty()) open();
    }
    public void open() {
        open = true;
    }

    @Override
    public void reset() {
        super.reset();
        open = false;
        keyName = originalArgs.next("");
        w = originalArgs.nextInt(1);
        h = originalArgs.nextInt(1);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable((double) w, h));
    }

    @Override
    public boolean isSolid(Entity entity) {
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
