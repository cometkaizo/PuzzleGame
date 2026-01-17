package com.cometkaizo.world.entity;

import com.cometkaizo.game.GameState;
import com.cometkaizo.screen.overlay.HermesOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable hermes sculpture
 */
public class HermesSculpture extends Interactable {
    private boolean open;

    public HermesSculpture(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

    @Override
    public void reset() {
        super.reset();
        open = originalGameState.hermesSolved;
    }

    @Override
    public void write(GameState state) {
        super.write(state);
        state.hermesSolved = open;
    }

    @Override
    protected void interact() {
        app.setOverlay(new HermesOverlay(app, open, () -> open = true));
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    @Override
    protected String getTexturePath() {
        return "sculpture/hermes";
    }

    @Override
    protected int getTextureDeltaX() {
        return super.getTextureDeltaX() - 16;
    }
}
