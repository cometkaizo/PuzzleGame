package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-05
 * Description: Screen overlay for the double-sided statue, Mephistopheles and Margaretta
 */
public class DoubleSidedOverlay extends Overlay {

    /// Creates a new overlay
    public DoubleSidedOverlay(GameApp app) {
        super(app);
    }

    /// Renders this overlay to the screen
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture(getTexturePath()));
    }

    /// Gets the path to the background texture
    private String getTexturePath() {
        return "gui/sculpture/double_sided/regular";
    }
}
