package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-06
 * Description: Screen overlay for the hope diamond
 */
public class HopeDiamondOverlay extends Overlay {
    private final boolean lit;

    /// Creates a new overlay
    public HopeDiamondOverlay(GameApp app, boolean lit) {
        super(app);
        this.lit = lit;
    }

    /// Renders this overlay
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture("gui/hope_diamond/" + (lit ? "lit" : "regular")));
    }
}
