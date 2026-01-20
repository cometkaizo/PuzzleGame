package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Text;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-04
 * Description: Screen overlay for narration (black screen with white text)
 */
public class NarrationOverlay extends Overlay {
    private final Text content;

    /// Creates a new overlay with the given message
    public NarrationOverlay(GameApp app, String message) {
        this(app, message, null);
    }
    /// Creates a new overlay with the given message and subsequent screen
    public NarrationOverlay(GameApp app, String message, Overlay next) {
        super(app, next);
        content = new Text(message, Assets.font("BoldPixels", 50), Color.WHITE,
                w -> w / 2,
                h -> h / 2,
                160, true, true);
    }

    /// Renders this overlay
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        content.render(canvas);
    }
}
