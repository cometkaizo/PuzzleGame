package com.cometkaizo.screen;

import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.world.Tickable;

import java.awt.*;
import java.util.function.BooleanSupplier;
import java.util.function.IntUnaryOperator;

public class Clickable implements Tickable, Renderable {
    protected BooleanSupplier action;
    protected int lastX, lastY, lastW, lastH;
    protected IntUnaryOperator x, y, w, h;

    public Clickable(BooleanSupplier action, IntUnaryOperator x, IntUnaryOperator y, IntUnaryOperator w, IntUnaryOperator h) {
        this.action = action;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    public Clickable(Runnable action, IntUnaryOperator x, IntUnaryOperator y, IntUnaryOperator w, IntUnaryOperator h) {
        this(() -> {
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
        lastX = (int) (x.applyAsInt(canvas.getPixelWidth()) * canvas.renderScale());
        lastY = (int) (y.applyAsInt(canvas.getPixelHeight()) * canvas.renderScale());
        lastW = (int) (w.applyAsInt(canvas.getPixelWidth()) * canvas.renderScale());
        lastH = (int) (h.applyAsInt(canvas.getPixelHeight()) * canvas.renderScale());
    }
}
