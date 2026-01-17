package com.cometkaizo.world.entity;

import com.cometkaizo.game.GameState;
import com.cometkaizo.screen.overlay.TheThinkerOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable "The Thinker" sculpture
 */
public class TheThinkerSculpture extends Interactable {
    private boolean brainSolved;
    private TheThinkerOverlay overlay;

    public TheThinkerSculpture(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(2D, 1D));
    }

    @Override
    public void reset() {
        super.reset();
        brainSolved = originalGameState.thinkerBrainSolved;
        overlay = new TheThinkerOverlay(app, brainSolved, () -> brainSolved = true);
    }

    @Override
    public void write(GameState state) {
        super.write(state);
        state.thinkerBrainSolved = brainSolved;
    }

    @Override
    protected void interact() {
        app.setOverlay(overlay);
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    @Override
    protected String getTexturePath() {
        return "sculpture/thinker";
    }
}
