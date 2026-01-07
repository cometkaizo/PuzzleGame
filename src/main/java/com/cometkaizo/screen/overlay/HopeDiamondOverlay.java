package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Screen overlay for the hope diamond
 */
public class HopeDiamondOverlay extends Overlay {
    private final boolean lit;

    public HopeDiamondOverlay(GameApp app, boolean lit) {
        super(app);
        this.lit = lit;
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture("gui/hope_diamond/" + (lit ? "lit" : "regular")));
    }
}
