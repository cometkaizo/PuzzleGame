package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.event.EventBus;
import com.cometkaizo.game.event.KeyPressedEvent;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.input.InputBindings;
import com.cometkaizo.screen.Renderable;
import com.cometkaizo.world.Tickable;

public abstract class Overlay implements Tickable, Renderable {
    protected final GameApp app;
    protected final EventBus eventBus;

    public Overlay(GameApp app) {
        this.app = app;
        eventBus = app.getOverlayEventBus();
        eventBus.register(KeyPressedEvent.class, this::maybeClose);
        eventBus.register(MousePressedEvent.class, this::onClick);
    }

    public void cleanup() {
        eventBus.unregister(KeyPressedEvent.class, this::maybeClose);
        eventBus.unregister(MousePressedEvent.class, this::onClick);
    }

    private void maybeClose(KeyPressedEvent click) {
        if (!shouldTickGame()) return; // do not close if there is no game beneath this overlay
        if (click.input() == InputBindings.OVERLAY_CLOSE.get()) app.setOverlay(null);
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

    /// helper method to get the mouse x position
    protected int mouseX() {
        return app.getMouseX();
    }
    /// helper method to get the mouse y position
    protected int mouseY() {
        return app.getMouseY();
    }
}
