package com.cometkaizo.world.entity;

import com.cometkaizo.game.GameState;
import com.cometkaizo.game.item.FeatherItem;
import com.cometkaizo.game.item.WeighableItem;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.overlay.AnubisOverlay;
import com.cometkaizo.screen.overlay.InventoryOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable anubis sculpture
 */
public class AnubisSculpture extends Interactable {
    private boolean scaleUnlocked;
    private WeighableItem weighed;
    public AnubisSculpture(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(1D, 1D));
    }

    @Override
    public void reset() {
        super.reset();
        scaleUnlocked = originalGameState.anubisScaleUnlocked;
        weighed = originalGameState.anubisWeighedItem;
    }

    @Override
    public void write(GameState state) {
        super.write(state);
        state.anubisWeighedItem = weighed;
        state.anubisScaleUnlocked = scaleUnlocked;
    }

    @Override
    protected void interact() {
        if (scaleUnlocked) app.setOverlay(new AnubisOverlay(app, weighed, this::onWeigh));
        else {
            app.narrate("To awaken Ra, Anubis seeks the feather of truth...", new InventoryOverlay(app, item -> {
                if (item instanceof FeatherItem) {
                    scaleUnlocked = true;
                    game.getInventory().remove(item);
                    if (room.getBlockOrEntity("statue of ra") instanceof RaSculpture ra) ra.turnOnLight();
                    app.narrate("Anubis accepts the truth. The statue of Ra awakens...", new AnubisOverlay(app, weighed, this::onWeigh));
                } else {
                    Assets.sound("wrong").play();
                    app.narrate("This is not the truth.", null);
                }
            }));
        }
    }

    private void onWeigh(WeighableItem item, AnubisOverlay.WeighResult weighResult) {
        this.weighed = item;
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    @Override
    protected String getTexturePath() {
        return "sculpture/anubis";
    }

    @Override
    protected int getTextureDeltaX() {
        return -2;
    }
    @Override
    protected int getTextureDeltaY() {
        return 2;
    }
}
