package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;

import java.awt.*;

public class LetterOverlay extends Overlay {

    public LetterOverlay(GameApp app) {
        super(app);
    }

    @Override
    public void render(Canvas canvas) {
        canvas.fillScreen(new Color(0, 0, 0, 200));
        canvas.renderImage(Assets.texture("gui/letter"), canvas.halfWidth(), canvas.halfHeight(), -0.5, -0.5);
    }

    @Override
    public void tick() {

    }
}
