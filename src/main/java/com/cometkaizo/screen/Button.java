package com.cometkaizo.screen;

import com.cometkaizo.app.GameApp;

import java.awt.*;
import java.util.function.IntUnaryOperator;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: A button that can be clicked on a screen overlay
 */
public class Button extends Clickable {
    protected String message;
    protected Font font;
    protected Color color = new Color(0, 0, 0);

    public Button(GameApp app, String message, int size, Runnable action, IntUnaryOperator x, IntUnaryOperator y, IntUnaryOperator w, IntUnaryOperator h) {
        super(app, action, x, y, w, h);
        this.message = message;
        this.font = Assets.font("BoldPixels", size);
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        renderMessage(canvas);
    }

    private void renderMessage(Canvas canvas) {
        canvas.renderString(message, font, color, lastX + lastW / 2F, lastY + lastH / 2F, true, true);
    }
}
