package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Text;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Screen overlay for a letter
 */
public class LetterOverlay extends Overlay {
    private final Text content;
    private final String variant;

    public LetterOverlay(GameApp app, String message, String variant) {
        super(app);
        content = new Text(message, Assets.font("BoldPixels", 20), Color.BLACK,
                w -> w / 2 - 48,
                h -> h / 2 - 60,
                94, false, false);
        this.variant = variant;
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderImage(Assets.texture("gui/" + variant), canvas.halfWidth(), canvas.halfHeight(), -0.5, -0.5);

        content.render(canvas);
    }
}
