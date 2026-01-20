package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.game.item.HeavyHeartItem;
import com.cometkaizo.screen.*;
import com.cometkaizo.screen.Canvas;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-13
 * Description: Screen overlay for the ludovisi ares sculpture
 */
public class LudovisiAresOverlay extends Overlay {
    private final Runnable chestAction;
    private final Runnable heartAction;
    private boolean chestOpen;
    private boolean heartOpen;

    /// Text
    private final Text text = new Text("E V I L\n2 4 1 8", Assets.font(24), Color.RED,
            w -> w/2 + 14, h -> h/2 - 55, 100, false, false);

    /// Hilt of the sword
    private final Clickable hilt = new ImageClickable(app, () -> {
        if (!chestOpen) app.setOverlay(new SwordOverlay(app, this::openChest, this));
    }, w -> w/2 - 31, h -> h/2 - 33, _ -> 30, _ -> 20, () -> "gui/sculpture/ares/hilt", -2, -2);

    /// Ares's chest
    private final Clickable chest = new ImageClickable(app, () -> {
        if (!heartOpen) app.setOverlay(new AresChestOverlay(app, this::openHeart, this));
    }, w -> w/2 + 2, h -> h/2 - 58, _ -> 30, _ -> 22, () -> "gui/sculpture/ares/chest", -17, -8);

    /// Creates a new overlay
    public LudovisiAresOverlay(GameApp app, boolean chestOpen, boolean heartOpen, Runnable chestAction, Runnable heartAction) {
        super(app);
        this.chestOpen = chestOpen;
        this.heartOpen = heartOpen;
        this.chestAction = chestAction;
        this.heartAction = heartAction;
    }

    /// Opens the chest
    private void openChest() {
        chestAction.run();
        chestOpen = true;
    }

    /// Opens the heart
    private void openHeart() {
        heartAction.run();
        heartOpen = true;
        app.getGame().getInventory().add(new HeavyHeartItem());
        app.narrate("The stone heart opens to reveal a human heart. You take it - it feels heavy.", null);
    }

    /// Renders this overlay to the screen
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture(getTexturePath()));

        if (chestOpen) chest.render(canvas);
        hilt.render(canvas);
        text.render(canvas);
    }

    /// Gets the path to the background texture
    private String getTexturePath() {
        return "gui/sculpture/ares/regular";
    }

    /// Ticks this overlay
    @Override
    public void tick() {
        super.tick();
        hilt.tick();
        if (chestOpen) chest.tick();
    }

    /// Called when the mouse is pressed
    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        hilt.onClick(click);
        if (chestOpen) chest.onClick(click);
    }
}
