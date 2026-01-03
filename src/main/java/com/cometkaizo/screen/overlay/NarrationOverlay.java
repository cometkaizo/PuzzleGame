package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Text;

import java.awt.*;

public class NarrationOverlay extends Overlay {
    private final Text content;

    public NarrationOverlay(GameApp app, String message) {
        this(app, message, null);
    }
    public NarrationOverlay(GameApp app, String message, Overlay prev) {
        super(app, prev);
        content = new Text(message, Assets.font("BoldPixels", 50), Color.WHITE,
                w -> w / 2,
                h -> h / 2,
                160, true, true);
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        content.render(canvas);
    }
}
