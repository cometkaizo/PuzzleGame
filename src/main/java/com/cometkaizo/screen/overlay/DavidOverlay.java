package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Clickable;
import com.cometkaizo.screen.ImageClickable;

public class DavidOverlay extends Overlay {
    private final Runnable openChestAction, openHeartAction;
    private boolean chestOpen, heartOpen;

    private final Clickable pedestal, chest;

    public DavidOverlay(GameApp app, boolean chestOpen, boolean heartOpen, boolean[][] pedestalCombo, Runnable openChestAction, Runnable openHeartAction) {
        super(app);
        this.chestOpen = chestOpen;
        this.heartOpen = heartOpen;
        this.pedestal = new ImageClickable(app, () -> {
            if (!this.chestOpen) app.setOverlay(new LeverLockOverlay(app, pedestalCombo, this::openChest, this));
        }, w -> w / 2 - 23, h -> h / 2 + 51, _ -> 55, _ -> 35, () -> "gui/sculpture/david/pedestal", -2, -2);
        this.chest = new ImageClickable(app, () -> {
            if (!this.heartOpen) app.setOverlay(new DavidChestOverlay(app, this::openHeart, this));
        }, w -> w / 2 - 29, h -> h / 2 - 60, _ -> 41, _ -> 32, () -> "gui/sculpture/david/chest", -2, -2);
        this.openChestAction = openChestAction;
        this.openHeartAction = openHeartAction;
    }

    public void openChest() {
        app.getOverlay().close(); // close lever lock overlay
        openChestAction.run();
        chestOpen = true;
    }
    public void openHeart() {
        openHeartAction.run();
        heartOpen = true;
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderImage(Assets.texture(getTexturePath()), canvas.halfWidth(), canvas.halfHeight(), -0.5, -0.5);
        pedestal.render(canvas);
        if (chestOpen) chest.render(canvas);
    }

    private String getTexturePath() {
        return "gui/sculpture/david/regular";
    }

    @Override
    public void tick() {
        super.tick();
        pedestal.tick();
        if (chestOpen) chest.tick();
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        pedestal.onClick(click);
        if (chestOpen) chest.onClick(click);
    }
}
