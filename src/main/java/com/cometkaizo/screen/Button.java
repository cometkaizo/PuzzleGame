package com.cometkaizo.screen;

import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.world.Tickable;

import java.awt.*;
import java.util.function.BooleanSupplier;
import java.util.function.IntUnaryOperator;

public class Button implements Tickable, Renderable {
    private static final Font BASE_FONT = Assets.font("BoldPixels");
    protected String message;
    protected Font font;
    protected Color color = new Color(0, 0, 0);
    protected BooleanSupplier action;
    private int lastX, lastY, lastW, lastH;
    public IntUnaryOperator x, y, w, h;

    public Button(String message, int size, BooleanSupplier action, IntUnaryOperator x, IntUnaryOperator y, IntUnaryOperator w, IntUnaryOperator h) {
        this.message = message;
        this.font = BASE_FONT.deriveFont(Font.PLAIN, size);
        this.action = action;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public boolean onClick(MousePressedEvent click) {
        if (contains(click.screenX(), click.screenY()))
            return action.getAsBoolean();
        return false;
    }

    private boolean contains(int x, int y) {
        return x >= lastX && x <= lastX + lastW && y >= lastY && y <= lastY + lastH;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Canvas canvas) {
        updatePosAndSize(canvas);

        canvas.getGraphics().drawRect(lastX, lastY, lastW, lastH);

        renderMessage(canvas);
    }

    private void updatePosAndSize(Canvas canvas) {
        lastX = x.applyAsInt(canvas.getWidth());
        lastY = y.applyAsInt(canvas.getHeight());
        lastW = w.applyAsInt(canvas.getWidth());
        lastH = h.applyAsInt(canvas.getHeight());
    }

    private void renderMessage(Canvas canvas) {
        canvas.renderString(message, font, color, lastX + lastW / 2F, lastY + lastH / 2F, true, true);
    }
}
