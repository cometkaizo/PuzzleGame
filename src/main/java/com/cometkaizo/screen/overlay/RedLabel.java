package com.cometkaizo.screen.overlay;

import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Renderable;
import com.cometkaizo.screen.Text;

import java.awt.*;
import java.util.function.IntUnaryOperator;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-19
 * Description: A red label that renders some text
 */
public class RedLabel implements Renderable {
    private final Text content;

    /// Creates a new label
    public RedLabel(String message, IntUnaryOperator x, IntUnaryOperator y) {
        content = new Text(message, Assets.font("BoldPixels", 40), Color.RED,
                x, y, 100, true, false);
    }

    /// Render this label to the screen
    @Override
    public void render(Canvas canvas) {
        content.render(canvas);
    }
}
