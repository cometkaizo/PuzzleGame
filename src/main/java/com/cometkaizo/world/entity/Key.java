package com.cometkaizo.world.entity;

import com.cometkaizo.game.item.EntranceKeyItem;
import com.cometkaizo.game.item.Item;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable key item
 */
public class Key extends Collectible {
    public Key(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    protected String pickupMessage() {
        return "You pick up the key.";
    }

    @Override
    protected Item newItem() {
        return new EntranceKeyItem();
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderDebugBoundingBox(boundingBox, Color.BLUE);
    }
    @Override
    protected String getTexturePath() {
        return "key";
    }

    @Override
    protected double getTextureDeltaYFactor() {
        return -2.5;
    }

    @Override
    public double getRenderY() {
        return position.y + 0.8;
    }
}
