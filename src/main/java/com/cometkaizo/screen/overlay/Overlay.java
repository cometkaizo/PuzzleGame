package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.event.EventBus;
import com.cometkaizo.game.event.KeyPressedEvent;
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
    }

    public void cleanup() {
        eventBus.unregister(KeyPressedEvent.class, this::maybeClose);
    }

    private void maybeClose(KeyPressedEvent click) {
        if (!shouldTickGame()) return; // do not close if there is no game beneath this overlay
        if (click.input() == InputBindings.OVERLAY_CLOSE.get()) app.setOverlay(null);
    }

    public boolean shouldTickGame() {
        return true;
    }
    public boolean shouldRenderGame() {
        return true;
    }
}
