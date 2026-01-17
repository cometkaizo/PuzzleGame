package com.cometkaizo.world.entity;

import com.cometkaizo.game.GameState;
import com.cometkaizo.game.item.LightHeartItem;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.overlay.VenusOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;
/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable statue of Venus de Milo sculpture
 */
public class VenusDeMiloSculpture extends Interactable {
    public static final String OPEN_HEART_MSG = """
            You insert the organ key into the heart and turn it.
            
            The stone heart opens to reveal a human heart. You take it - it feels lighter than a feather.""";
    private boolean[][] pedestalCombo = {{true, true, false, false, false, true, true}, {false, true, true, true, true, true, false}};
    private boolean chestOpen, heartOpen;

    public VenusDeMiloSculpture(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(2D, 1D));
    }

    @Override
    public void reset() {
        super.reset();
        chestOpen = originalGameState.venusChestOpen;
        heartOpen = originalGameState.venusHeartOpen;
    }

    @Override
    public void write(GameState state) {
        super.write(state);
        state.venusChestOpen = chestOpen;
        state.venusHeartOpen = heartOpen;
    }

    @Override
    protected void interact() {
        app.setOverlay(new VenusOverlay(app, chestOpen, heartOpen, pedestalCombo, this::openChest, this::openHeart));
    }

    public void openChest() {
        chestOpen = true;
    }
    public void openHeart() {
        heartOpen = true;
        app.narrate(OPEN_HEART_MSG, null);
        game.getInventory().add(new LightHeartItem());
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderDebugBoundingBox(boundingBox, Color.BLUE);
    }

    @Override
    protected String getTexturePath() {
        return "sculpture/venus";
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }
}
