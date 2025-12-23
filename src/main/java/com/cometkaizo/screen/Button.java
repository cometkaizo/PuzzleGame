package com.cometkaizo.screen;

import java.awt.*;
import java.util.function.BooleanSupplier;
import java.util.function.IntUnaryOperator;

public class Button extends Clickable {
    protected String message;
    protected Font font;
    protected Color color = new Color(0, 0, 0);

    public Button(String message, int size, BooleanSupplier action, IntUnaryOperator x, IntUnaryOperator y, IntUnaryOperator w, IntUnaryOperator h) {
        super(action, x, y, w, h);
        this.message = message;
        this.font = Assets.font("BoldPixels", size);
        this.action = action;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
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
