package com.cometkaizo.world.entity;

import com.cometkaizo.screen.Canvas;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Light;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;

public class RaSculpture extends Interactable {
    private Light.Direction direction = Light.Direction.W;
    private boolean emittingLight;
    public RaSculpture(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    protected void interact() {
        emittingLight = !emittingLight;
    }

    @Override
    public void tick() {
        super.tick();
        if (emittingLight) layer.lightUp(lightEmissionPos(), direction);
        else layer.unlight(lightEmissionPos(), direction);
    }
    private Vector.ImmutableInt lightEmissionPos() {
        return Vector.immutableInt(position).addedTo(0, 1).addedTo(direction.delta);
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderDebugBoundingBox(boundingBox, Color.BLUE);
    }

    @Override
    protected String getTexturePath() {
        return "sculpture/ra";
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
    public boolean isSolid(Entity entity) {
        return true;
    }
}
