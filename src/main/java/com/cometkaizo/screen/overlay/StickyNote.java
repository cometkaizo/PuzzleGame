package com.cometkaizo.screen.overlay;

import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Renderable;
import com.cometkaizo.screen.Text;

import java.awt.*;
import java.util.function.IntUnaryOperator;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-07
 * Description: Sticky notes that can contain some text
 */
public class StickyNote implements Renderable {
    private final Text content;
    private final String variant;
    private final IntUnaryOperator x, y;

    public StickyNote(String message, String variant, IntUnaryOperator x, IntUnaryOperator y) {
        this.x = x;
        this.y = y;
        content = new Text(message, Assets.font("BoldPixels", 40/*20*/), Color.RED,
                x, y, 100, true, false);
        this.variant = variant;
    }

    @Override
    public void render(Canvas canvas) {
        /*canvas.renderImage(Assets.texture("gui/sticky_note/" + variant),
                canvas.scale(x.applyAsInt(canvas.getPixelWidth())),
                canvas.scale(y.applyAsInt(canvas.getPixelHeight()) - 1),
                -0.5, 0);*/

        content.render(canvas);
    }
}
