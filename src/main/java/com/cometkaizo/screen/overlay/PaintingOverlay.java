package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Text;

import java.awt.*;

public class PaintingOverlay extends Overlay {
    private final Text content;
    private final String variant;

    public PaintingOverlay(GameApp app, String variant, String label) {
        super(app);
        content = new Text(label, Assets.font("BoldPixels", 30), Color.WHITE,
                w -> w / 2,
                h -> h / 2 + 75,
                280, true, false);
        this.variant = variant;
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderImage(Assets.texture("gui/painting/" + variant), canvas.halfWidth(), canvas.scale(5), -0.5, 0);

        content.render(canvas);
    }
}
