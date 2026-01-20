package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.*;
import com.cometkaizo.screen.Canvas;

import java.awt.*;
import java.util.List;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-08
 * Description: Screen overlay for the statue of Venus de Milo
 */
public class VenusOverlay extends Overlay {
    private final Runnable openChestAction, openHeartAction;
    private boolean chestOpen, heartOpen;

    private final Clickable pedestal, chest;
    private final List<Writing> writing = List.of(
            new Writing("8 5 1 4", 16, -83),
            new Writing("19 8 15 21 12 4 5 18", -56, -67),
            new Writing("11 14 5 5", 2, 5)
    );

    /// Creates a new overlay
    public VenusOverlay(GameApp app, boolean chestOpen, boolean heartOpen, boolean[][] pedestalCombo, Runnable openChestAction, Runnable openHeartAction) {
        super(app);
        this.chestOpen = chestOpen;
        this.heartOpen = heartOpen;
        this.pedestal = new ImageClickable(app, () -> {
            if (!this.chestOpen) app.setOverlay(new LeverLockOverlay(app, pedestalCombo, this::openChest, this));
        }, w -> w / 2 - 23, h -> h / 2 + 51, _ -> 55, _ -> 35, () -> "gui/sculpture/venus/pedestal", -2, -2);
        this.chest = new ImageClickable(app, () -> {
            if (!this.heartOpen) app.setOverlay(new VenusChestOverlay(app, this::openHeart, this));
        }, w -> w / 2 - 14, h -> h / 2 - 69, _ -> 30, _ -> 32, () -> "gui/sculpture/venus/chest", -13, -8);
        this.openChestAction = openChestAction;
        this.openHeartAction = openHeartAction;
    }

    /// Opens the chest
    public void openChest() {
        app.setOverlay(new NarrationOverlay(app, "You hear the squeaking of hinges. The chest of Venus de Milo swings open to reveal stone organs within.", this));
        openChestAction.run();
        chestOpen = true;
    }
    /// Opens the heart
    public void openHeart() {
        openHeartAction.run();
        heartOpen = true;
    }

    /// Renders this overlay to the screen
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderImage(Assets.texture(getTexturePath()), canvas.halfWidth(), canvas.halfHeight(), -0.5, -0.5);
        pedestal.render(canvas);
        if (chestOpen) chest.render(canvas);
        for (var w : writing) w.render(canvas);
    }

    /// Gets the path to the background texture
    private String getTexturePath() {
        return "gui/sculpture/venus/regular";
    }

    /// Ticks this overlay
    @Override
    public void tick() {
        super.tick();
        pedestal.tick();
        if (chestOpen) chest.tick();
    }

    /// Called when the mouse is pressed
    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        pedestal.onClick(click);
        if (chestOpen) chest.onClick(click);
    }

    /// A single piece of red writing on the sculpture
    private static class Writing extends Text {
        public Writing(String message, int dx, int dy) {
            super(message, Assets.font("BoldPixels", 30), Color.RED,
                    w -> w/2 + dx, h -> h/2 + dy, 100, true, false);
        }
    }
}
