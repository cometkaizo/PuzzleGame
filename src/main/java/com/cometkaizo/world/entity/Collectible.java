package com.cometkaizo.world.entity;

import com.cometkaizo.game.item.Item;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;
/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: Interactable collectible item
 */
public abstract class Collectible extends Interactable {

    protected boolean collected;

    /// Creates a new collectible entity
    public Collectible(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        this.boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

    /// Opens the interaction overlay when the player interacts with this entity
    @Override
    protected void interact() {
        room.player.onInteract();
        collected = true;
        app.narrate(pickupMessage(), null);
        game.getInventory().add(newItem());
        Assets.sound("notify").play();
    }
    /// Gets the message that is displayed when picked up
    protected abstract String pickupMessage();
    /// Creates a new item that is picked up
    protected abstract Item newItem();

    /// Returns whether this entity can currently be interacted
    @Override
    protected boolean canBeInteracted() {
        return super.canBeInteracted() && !collected();
    }

    /// Returns whether this entity has already been collected
    public boolean collected() {
        return collected;
    }

    /// Renders this entity to the screen
    @Override
    public void render(Canvas canvas) {
        var g = canvas.getGraphics();
        var oT = g.getTransform();
        var oC = g.getComposite();

        int screenX = canvas.toScreenX(canvas.lerp(getOldX(), getX()) + 1);
        int screenY = canvas.toScreenY(canvas.lerp(getOldY(), getY()) + 0.5);

        double translateX = 0, translateY = 0;
        double alpha = 1;

        if (collected()) return;

        {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            g.translate(translateX, translateY);
            g.translate(screenX, screenY);
            g.translate(-screenX, -screenY);
        }

        super.render(canvas);

        g.setTransform(oT);
        g.setComposite(oC);
    }
}
