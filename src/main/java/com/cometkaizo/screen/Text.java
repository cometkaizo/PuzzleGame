package com.cometkaizo.screen;

import com.cometkaizo.util.StringUtils;

import java.awt.*;
import java.util.List;
import java.util.function.IntUnaryOperator;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-22
 * Description: Renderable text component which handles all line-breaking logic. An instance of this class
 * can be rendered at placed at any x and y, and with any width, font, or color.
 */
public class Text implements Renderable {
    public List<String> lines;
    public final String message;
    private final Font font;
    private final Color color;
    private final IntUnaryOperator x, y;
    private final int w;
    private final boolean centerX, centerY;

    /// Creates a new text component
    public Text(String message, Font font, Color color, IntUnaryOperator x, IntUnaryOperator y, int w, boolean centerX, boolean centerY) {
        this.message = message;
        this.font = font;
        this.color = color;
        this.x = x;
        this.y = y;
        this.w = w;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /// Renders this text onto the screen
    @Override
    public void render(Canvas canvas) {
        // cannot do initialization of lines in the constructor because we don't have
        // access to a Canvas instance (or we could pass in a Game instance to constructor)
        if (lines == null) initLines(canvas);

        int totalHeight = font.getSize() * lines.size();
        int yOffset = centerY ? -totalHeight / 2 : 0;

        for (int lineId = 0; lineId < lines.size(); lineId++) {
            String line = lines.get(lineId);
            int lineOffset = font.getSize() * (lineId + 1);
            canvas.renderString(line, font, color,
                    (float) (x.applyAsInt(canvas.getPixelWidth()) * canvas.renderScale()),
                    (float) (y.applyAsInt(canvas.getPixelHeight()) * canvas.renderScale() + lineOffset) + yOffset,
                    centerX, false);
        }
    }

    /// initializes the lines using the given canvas
    private void initLines(Canvas canvas) {
        lines = StringUtils.createLines(message, canvas.getGraphics().getFontMetrics(font), canvas.scale(w));
    }
}
