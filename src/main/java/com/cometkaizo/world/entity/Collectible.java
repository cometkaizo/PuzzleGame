package com.cometkaizo.world.entity;

import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;

// todo: make inventory system and connect it with these items
public abstract class Collectible extends Interactable {

    protected final int collectDuration = 5;
    protected int collectTime = -1;

    public Collectible(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position.add(0.5, 0D), args);
        this.boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }


    @Override
    protected void interact() {
        room.player.displayedCollectible = this;
        room.player.onInteract();
        collectTime = 0;
        game.collect(this);
        Assets.sound("notify").play();
    }

    @Override
    protected boolean canBeInteracted() {
        return super.canBeInteracted() && !collected();
    }

    public boolean collected() {
        return collectTime > -1;
    }

    @Override
    public void tick() {
        super.tick();
        if (collected() && collectTime < collectDuration) collectTime ++;
    }

    @Override
    public boolean canCollideWhenMoving() {
        return false;
    }

    @Override
    public void render(Canvas canvas) {
        var g = canvas.getGraphics();
        var oT = g.getTransform();
        var oC = g.getComposite();

        int screenX = canvas.toScreenX(canvas.lerp(getOldX(), getX()) + 0.5);
        int screenY = canvas.toScreenY(canvas.lerp(getOldY(), getY()) + 0.5);

        double angle = 0;
        double translateX = 0, translateY = 0;
        double alpha = 1;

        double playerScreenX = canvas.toScreenX(canvas.lerp(room.player.getOldX(), room.player.getX()));
        double playerScreenY = canvas.toScreenY(canvas.lerp(room.player.getOldY(), room.player.getY()));

        if (!collected()) {

        } else if (room.player.displayedCollectible == this) {
            translateX = playerScreenX - screenX + canvas.toScreenLength(0.63);
            translateY = playerScreenY - screenY - canvas.toScreenLength(2.2) * (1 - Math.pow(1 - Math.min(1, (collectTime + canvas.partialTick()) / collectDuration), 2.5));
        } else return;

        {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            g.translate(translateX, translateY);
            g.translate(screenX, screenY);
            if (angle != 0)
                g.rotate(angle);
            g.translate(-screenX, -screenY);
        }

        super.render(canvas);

        g.setTransform(oT);
        g.setComposite(oC);
    }
}
