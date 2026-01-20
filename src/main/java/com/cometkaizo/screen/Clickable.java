package com.cometkaizo.screen;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.world.Tickable;

import java.awt.*;
import java.util.function.BooleanSupplier;
import java.util.function.IntUnaryOperator;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-04
 * Description: A 2D rectangle on the screen which performs an action when clicked
 */
public class Clickable implements Tickable, Renderable {
    protected final GameApp app;
    protected BooleanSupplier action;
    protected int lastX, lastY, lastW, lastH;
    protected IntUnaryOperator x, y, w, h;

    /// Creates a new clickable
    public Clickable(GameApp app, BooleanSupplier action, IntUnaryOperator x, IntUnaryOperator y, IntUnaryOperator w, IntUnaryOperator h) {
        this.app = app;
        this.action = action;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    /// Creates a new clockable
    public Clickable(GameApp app, Runnable action, IntUnaryOperator x, IntUnaryOperator y, IntUnaryOperator w, IntUnaryOperator h) {
        this(app, () -> {
            action.run();
            return true;
        }, x, y, w, h);
    }

    /// Called when the mouse is pressed.
    /// Performs the action given in the constructor.
    public boolean onClick(MousePressedEvent click) {
        if (contains(click.screenX(), click.screenY())) {
            return action.getAsBoolean();
        }
        return false;
    }

    /// Returns whether this clickable is hovered
    public boolean isHovered() {
        return contains(app.getMouseX(), app.getMouseY());
    }

    /// Returns whether the given pixel coordinates are within this clickable's bounding box
    public boolean contains(int x, int y) {
        return x >= lastX && x <= lastX + lastW && y >= lastY && y <= lastY + lastH;
    }

    /// Ticks this clickable
    @Override
    public void tick() {

    }

    /// Renders this clickable to the screen
    @Override
    public void render(Canvas canvas) {
        updatePosAndSize(canvas);

        canvas.renderDebugRect(lastX, lastY, lastW, lastH, Color.RED);
    }

    /// Updates the position and size using the functions given in the constructor
    private void updatePosAndSize(Canvas canvas) {
        lastX = canvas.scale(x.applyAsInt(canvas.getPixelWidth()));
        lastY = canvas.scale(y.applyAsInt(canvas.getPixelHeight()));
        lastW = canvas.scale(w.applyAsInt(canvas.getPixelWidth()));
        lastH = canvas.scale(h.applyAsInt(canvas.getPixelHeight()));
    }
}
