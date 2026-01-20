package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Text;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-04
 * Description: Screen overlay for paintings
 */
public class PaintingOverlay extends Overlay {
    private final boolean litUp;
    private final Text content;
    private final String variant;

    /// Creates a new overlay
    public PaintingOverlay(GameApp app, boolean litUp, String variant, String label) {
        super(app);
        this.litUp = litUp;
        content = new Text(label, Assets.font("BoldPixels", 30), Color.WHITE,
                w -> w / 2,
                h -> h / 2 + 75,
                280, true, false);
        this.variant = variant;
    }

    /// Renders this overlay to the screen
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderImage(Assets.texture("gui/painting/" + variant + (litUp ? "_lit" : "")), canvas.halfWidth(), canvas.scale(5), -0.5, 0);

        content.render(canvas);
    }
}
