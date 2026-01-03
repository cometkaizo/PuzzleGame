package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.event.EventBus;
import com.cometkaizo.game.event.KeyPressedEvent;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.input.InputBindings;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Renderable;
import com.cometkaizo.world.Tickable;

import java.awt.*;

public abstract class Overlay implements Tickable, Renderable {
    protected final GameApp app;
    protected final EventBus eventBus;
    protected Overlay next;

    public Overlay(GameApp app) {
        this(app, null);
    }
    public Overlay(GameApp app, Overlay next) {
        this.app = app;
        this.next = next;
        eventBus = app.getOverlayEventBus();
    }

    /// Called whenever this overlay becomes visible on the screen
    public void setup() {
        eventBus.register(this, KeyPressedEvent.class, this::maybeClose);
        eventBus.register(this, MousePressedEvent.class, this::onClick);
    }

    public void cleanup() {
        eventBus.unregister(this);
    }

    protected void maybeClose(KeyPressedEvent click) {
        if (!shouldTickGame()) return; // do not close if there is no game beneath this overlay
        if (click.input() == InputBindings.OVERLAY_CLOSE.get()) close();
    }
    public void close() {
        app.setOverlay(next);
    }

    protected void onClick(MousePressedEvent click) { }

    public boolean shouldTickGame() {
        return true;
    }
    public boolean shouldRenderGame() {
        return true;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Canvas canvas) {
        canvas.fillScreen(new Color(0, 0, 0, 255));
    }

    /// helper method to get the mouse x position
    protected int mouseX() {
        return app.getMouseX();
    }
    /// helper method to get the mouse y position
    protected int mouseY() {
        return app.getMouseY();
    }
}
