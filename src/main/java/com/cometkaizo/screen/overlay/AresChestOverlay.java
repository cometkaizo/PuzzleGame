package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Clickable;
import com.cometkaizo.screen.ImageClickable;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Screen overlay for the statue of Ludovisi Ares chest
 */
public class AresChestOverlay extends Overlay {
    private final Runnable openHeartAction;
    private boolean heartOpen;

    private final Clickable heart;

    public AresChestOverlay(GameApp app, Runnable openHeartAction, Overlay next) {
        super(app, next);
        this.openHeartAction = openHeartAction;
        heart = new ImageClickable(this.app, this::openHeart, w -> w/2 - 6, h -> h/2 - 23, _ -> 10, _ -> 27, () -> "gui/sculpture/ares/heart", -2, -2);
    }

    private void openHeart() {
        if (heartOpen) return;
        openHeartAction.run();
        heartOpen = true;
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderImage(Assets.texture("gui/sculpture/ares/chest_closeup"), canvas.halfWidth(), canvas.halfHeight(), -0.5, -0.5);
        heart.render(canvas);
    }

    @Override
    public void tick() {
        super.tick();
        heart.tick();
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        heart.onClick(click);
    }
}
