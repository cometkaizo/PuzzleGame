package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Text;

import java.awt.*;
import java.util.function.BooleanSupplier;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Screen overlay for the morse code poster
 */
public class MorseCodePosterOverlay extends Overlay {
    private final Text content;
    private final BooleanSupplier isLit;

    public MorseCodePosterOverlay(GameApp app, BooleanSupplier isLit) {
        super(app);
        this.isLit = isLit;
        content = new Text("""
                """, Assets.font(20), Color.BLACK,
                w -> w / 2 - 48,
                h -> h / 2 - 60,
                94, false, false);
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture("gui/morse_code_poster/regular"));

        // render the light if the poster is currently lit
        if (isLit.getAsBoolean()) {
            int x = canvas.scale(canvas.halfPixelWidth());
            int y = canvas.scale(canvas.halfPixelHeight());
            canvas.renderImage(Assets.texture("gui/morse_code_poster/light"), x, y);
        }

        content.render(canvas);
    }
}
