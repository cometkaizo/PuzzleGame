package com.cometkaizo.screen;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.Game;
import com.cometkaizo.game.GameSettings;
import com.cometkaizo.world.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static com.cometkaizo.app.GameDriver.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-05
 * Description: Class for rendering the game and screen overlay using Canvas
 */
public class GameRenderer extends JPanel {

    private final Canvas canvas;
    private final GameApp app;
    private final Dimension size;
    private double partialTick;
    private int mouseX, mouseY;
    private boolean mouseDown;
    private long lastTickTimeMillis, secondLastTickTimeMillis, lastRenderTimeMillis;

    /// Creates a new game renderer
    public GameRenderer(Settings settings, GameApp app) {
        this.app = app;

        GameSettings gameSettings = game().getSettings();

        canvas = new Canvas(game().getCameraPosition().x,
                game().getCameraPosition().y,
                gameSettings.unscaledTileSize,
                gameSettings.renderScale,
                null);

        setPreferredSize(new Dimension((int) gameSettings.widthInTiles * gameSettings.tileSize,
                (int) gameSettings.heightInTiles * gameSettings.tileSize));
        setBackground(settings.backgroundColor);
        this.size = getSize();
    }

    /// Paints the application to the window
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        render((Graphics2D) g);
        g.dispose();
    }

    /// Updates this game renderer with the number of milliseconds the last tick took to complete
    public void tick(long lastTickTimeMillis) {
        this.secondLastTickTimeMillis = this.lastTickTimeMillis;
        this.lastTickTimeMillis = lastTickTimeMillis;
        updatePartialTick();
    }

    /// Renders the application to the screen
    protected void render(Graphics2D g) {
        updatePartialTick();

        Dimension size = getSize(this.size);
        g.setClip(0, 0, size.width, size.height);

        if (app.shouldRenderGame()) renderGame(g, size);
        if (app.shouldTickOrRenderOverlay()) renderOverlay(g, size);

        lastRenderTimeMillis = System.currentTimeMillis();

        g.dispose();
    }

    /// Updates the partial tick according to the amount of time since last tick
    private void updatePartialTick() {
        this.partialTick = (double) millisSinceLastTick() / TICK_PERIOD;
    }
    /// Returns the number of milliseconds since the last tick occurred
    private long millisSinceLastTick() {
        return System.currentTimeMillis() - lastTickTimeMillis;
    }

    /// Renders the game to the screen
    private void renderGame(Graphics2D g, Dimension size) {
        canvas.startRender(g, game().getPrevCameraPosition(), game().getCameraPosition(), size.width, size.height, partialTick);

        game().render(canvas);
        renderDebugPerformanceMetrics();

        canvas.endRender();
    }

    /// Renders the screen overlay to the screen
    private void renderOverlay(Graphics2D g, Dimension size) {
        canvas.startRender(g, Vector.immutable(0D, 0D), Vector.immutable(0D, 0D), size.width, size.height, partialTick);

        app.getOverlay().render(canvas);
        renderDebugMousePos();
        renderDebugPerformanceMetrics();

        canvas.endRender();
    }

    /// Renders the mouse position if in debug mode
    private void renderDebugMousePos() {
        int mouseXFromCenter = (int) ((mouseX - canvas.halfWidth()) / canvas.renderScale());
        int mouseYFromCenter = (int) ((mouseY - canvas.halfHeight()) / canvas.renderScale());
        canvas.renderDebugString(mouseXFromCenter + ", " + mouseYFromCenter, Color.PINK, 10, 34);
    }
    /// Renders the TPS and FPS if in debug mode
    private void renderDebugPerformanceMetrics() {
        long lastTickDurationMillis = lastTickTimeMillis - secondLastTickTimeMillis;
        int tps = lastTickDurationMillis == 0 ? TPS : (int) (1000 / lastTickDurationMillis);
        canvas.renderDebugString("%7d TPS (%3d ms)".formatted(tps, lastTickDurationMillis), Color.GRAY, canvas.getWidth() - 250, 34);

        long renderTimeMillis = System.currentTimeMillis() - lastRenderTimeMillis;
        int fps = renderTimeMillis == 0 ? FPS : (int) (1000 / renderTimeMillis);
        canvas.renderDebugString("%7d FPS (%3d ms)".formatted(fps, renderTimeMillis), Color.GRAY, canvas.getWidth() - 250, 68);
    }

    /// Toggles debug mode
    public void toggleDebug() {
        canvas.setDebug(!canvas.isDebug());
    }

    /// Gets the game
    private Game game() {
        return app.getGame();
    }

    /// Updates the mouse position when it moves
    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        super.processMouseMotionEvent(e);
        mouseX = e.getX();
        mouseY = e.getY();
    }

    /// Updates whether the mouse is down
    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                mouseDown = true;
            } else if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                mouseDown = false;
            }
        }
    }

    /// Gets the mouse x
    public int getMouseX() {
        return mouseX;
    }
    /// Gets the mouse y
    public int getMouseY() {
        return mouseY;
    }
    /// Gets whether the mouse is down currently
    public boolean isMouseDown() {
        return mouseDown;
    }

    /// Settings for the game renderer
    public record Settings(
            Dimension size,
            Color backgroundColor
    ) {}
}
