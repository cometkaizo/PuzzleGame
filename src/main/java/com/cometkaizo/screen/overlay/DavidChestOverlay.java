package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.game.item.OrganKeyItem;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Clickable;
import com.cometkaizo.screen.ImageClickable;

public class DavidChestOverlay extends Overlay {
    private final Runnable openHeartAction;
    private boolean heartOpen;

    private final Clickable keyhole;

    public DavidChestOverlay(GameApp app, Runnable openHeartAction, Overlay prev) {
        super(app, prev);
        this.openHeartAction = openHeartAction;
        keyhole = new ImageClickable(this.app, this::tryOpenHeart, w -> w/2 - 9, h -> h/2 - 22, _ -> 16, _ -> 16, () -> "gui/sculpture/david/keyhole", 0, 0);
    }

    public void tryOpenHeart() {
        app.setOverlay(new InventoryOverlay(app, item -> {
            if (item instanceof OrganKeyItem) openHeart();
        }, this));
    }
    private void openHeart() {
        openHeartAction.run();
        heartOpen = true;
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderImage(Assets.texture("gui/sculpture/david/chest_closeup"), canvas.halfWidth(), canvas.halfHeight(), -0.5, -0.5);
        keyhole.render(canvas);
    }

    @Override
    public void tick() {
        super.tick();
        keyhole.tick();
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        keyhole.onClick(click);
    }
}
