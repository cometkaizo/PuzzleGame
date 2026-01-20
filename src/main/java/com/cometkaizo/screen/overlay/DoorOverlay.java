package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.game.item.Item;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Clickable;
import com.cometkaizo.screen.ImageClickable;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-08
 * Description: Screen overlay for a door
 */
public class DoorOverlay extends Overlay {
    private final Class<? extends Item> type;
    private final Runnable openAction;
    private final Clickable keyhole;

    /// Creates a new overlay
    public DoorOverlay(GameApp app, Class<? extends Item> type, Runnable openAction) {
        super(app);
        this.type = type;
        this.openAction = openAction;
        keyhole = new ImageClickable(this.app, this::useKeyhole, w -> w/2 - 8, h -> h/2 - 8, _ -> 16, _ -> 16, () -> "gui/door/keyhole", 0, 0);
    }

    /// Attempts to open this door by using an item on the keyhole
    public void useKeyhole() {
        app.setOverlay(new InventoryOverlay(app, item -> {
            if (type.isInstance(item)) open();
            else Assets.sound("wrong").play();
        }, this));
    }
    /// Actually opens this door
    private void open() {
        openAction.run();
        app.setOverlay(null);
    }

    /// Renders this overlay
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderImage(Assets.texture("gui/door/door"), canvas.halfWidth(), canvas.halfHeight(), -0.5, -0.5);
        keyhole.render(canvas);
    }

    /// Ticks this overlay
    @Override
    public void tick() {
        super.tick();
        keyhole.tick();
    }

    /// Called when the mouse is clicked
    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        keyhole.onClick(click);
    }
}
