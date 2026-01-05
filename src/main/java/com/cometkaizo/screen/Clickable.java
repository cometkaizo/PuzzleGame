package com.cometkaizo.screen;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.world.Tickable;

import java.awt.*;
import java.util.function.BooleanSupplier;
import java.util.function.IntUnaryOperator;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: A 2D rectangle which performs an action when clicked
 */
public class Clickable implements Tickable, Renderable {
    protected final GameApp app;
    protected BooleanSupplier action;
    protected int lastX, lastY, lastW, lastH;
    protected IntUnaryOperator x, y, w, h;

    public Clickable(GameApp app, BooleanSupplier action, IntUnaryOperator x, IntUnaryOperator y, IntUnaryOperator w, IntUnaryOperator h) {
        this.app = app;
        this.action = action;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    public Clickable(GameApp app, Runnable action, IntUnaryOperator x, IntUnaryOperator y, IntUnaryOperator w, IntUnaryOperator h) {
        this(app, () -> {
            action.run();
            return true;
        }, x, y, w, h);
    }

    public boolean onClick(MousePressedEvent click) {
        if (contains(click.screenX(), click.screenY())) {
            return action.getAsBoolean();
        }
        return false;
    }

    public boolean isHovered() {
        return contains(app.getMouseX(), app.getMouseY());
    }

    public boolean contains(int x, int y) {
        return x >= lastX && x <= lastX + lastW && y >= lastY && y <= lastY + lastH;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Canvas canvas) {
        updatePosAndSize(canvas);

        canvas.renderDebugRect(lastX, lastY, lastW, lastH, Color.RED);
    }

    private void updatePosAndSize(Canvas canvas) {
        lastX = canvas.scale(x.applyAsInt(canvas.getPixelWidth()));
        lastY = canvas.scale(y.applyAsInt(canvas.getPixelHeight()));
        lastW = canvas.scale(w.applyAsInt(canvas.getPixelWidth()));
        lastH = canvas.scale(h.applyAsInt(canvas.getPixelHeight()));
    }
}
