package com.cometkaizo.screen;

import com.cometkaizo.world.Tickable;

public interface Overlay extends Tickable, Renderable {
    boolean shouldTickGame();
    boolean shouldRenderGame();

    void cleanup();
}
