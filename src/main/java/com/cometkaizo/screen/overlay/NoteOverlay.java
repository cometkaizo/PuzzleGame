package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Text;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-16
 * Description: Screen overlay for a note or letter with no title
 */
public class NoteOverlay extends Overlay {
    private final Text content;
    private final String variant;

    public NoteOverlay(GameApp app, String message, String variant) {
        super(app);
        content = new Text(message, Assets.font(20), Color.BLACK,
                w -> w / 2 - 48,
                h -> h / 2 - 60,
                94, false, false);
        this.variant = variant;
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture("gui/note/" + variant));

        content.render(canvas);
    }
}
