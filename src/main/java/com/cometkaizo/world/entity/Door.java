package com.cometkaizo.world.entity;

import com.cometkaizo.game.item.ChessKeyItem;
import com.cometkaizo.game.item.EntranceKeyItem;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.overlay.DoorOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;
/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable door
 */
public class Door extends Interactable {
    private int w, h;
    private boolean open;
    private String roomName;
    public Door(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    protected void interact() {
        if (open) return;
        if (this == game.libraryDoor) app.setOverlay(new DoorOverlay(app, EntranceKeyItem.class, this::open));
        else if (this == game.chessDoor) app.setOverlay(new DoorOverlay(app, ChessKeyItem.class, this::open));
        else app.narrate(getRoomNameMessage() + "It's locked.", null);
    }
    private String getRoomNameMessage() {
        return roomName == null ? "" : "The door to the " + roomName + ".\n\n";
    }

    @Override
    protected void solve() {
        open();
    }
    public void open() {
        if (open) return;
        Assets.sound("door").play();
        open = true;
    }

    @Override
    public void reset() {
        super.reset();
        open = false;
        w = originalArgs.nextInt(1);
        h = originalArgs.nextInt(1);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable((double) w, h));

        roomName = originalArgs.next(null);
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
        super.render(canvas);
        canvas.renderDebugBoundingBox(boundingBox, open ? Color.GREEN : Color.PINK);
    }

    @Override
    protected String getTexturePath() {
        return "door/" + (open ? "open/" : "closed/") + name;
    }

    @Override
    protected int getTextureDeltaX() {
        return -2;
    }
    @Override
    protected int getTextureDeltaY() {
        return 2;
    }

    @Override
    public double getRenderY() {
        return position.y + 0.8;
    }
}
