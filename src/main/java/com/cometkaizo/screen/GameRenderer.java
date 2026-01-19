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

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        render((Graphics2D) g);
        g.dispose();
    }

    public void tick(long lastTickTimeMillis) {
        this.secondLastTickTimeMillis = this.lastTickTimeMillis;
        this.lastTickTimeMillis = lastTickTimeMillis;
        updatePartialTick();
    }

    protected void render(Graphics2D g) {
        updatePartialTick();

        Dimension size = getSize(this.size);
        g.setClip(0, 0, size.width, size.height);

        if (app.shouldRenderGame()) renderGame(g, size);
        if (app.shouldTickOrRenderOverlay()) renderOverlay(g, size);

        lastRenderTimeMillis = System.currentTimeMillis();
    }

    private void updatePartialTick() {
        this.partialTick = (double) millisSinceLastTick() / TICK_PERIOD;
    }
    private long millisSinceLastTick() {
        return System.currentTimeMillis() - lastTickTimeMillis;
    }

    private void renderGame(Graphics2D g, Dimension size) {
        canvas.startRender(g, game().getPrevCameraPosition(), game().getCameraPosition(), size.width, size.height, partialTick);

        game().render(canvas);
        renderDebugPerformanceMetrics();

        canvas.endRender();
    }

    private void renderOverlay(Graphics2D g, Dimension size) {
        canvas.startRender(g, Vector.immutable(0D, 0D), Vector.immutable(0D, 0D), size.width, size.height, partialTick);

        app.getOverlay().render(canvas);
        renderDebugMousePos();
        renderDebugPerformanceMetrics();

        canvas.endRender();
    }

    private void renderDebugMousePos() {
        int mouseXFromCenter = (int) ((mouseX - canvas.halfWidth()) / canvas.renderScale());
        int mouseYFromCenter = (int) ((mouseY - canvas.halfHeight()) / canvas.renderScale());
        canvas.renderDebugString(mouseXFromCenter + ", " + mouseYFromCenter, Color.PINK, 10, 34);
    }
    private void renderDebugPerformanceMetrics() {
        long lastTickDurationMillis = lastTickTimeMillis - secondLastTickTimeMillis;
        int tps = lastTickDurationMillis == 0 ? TPS : (int) (1000 / lastTickDurationMillis);
        canvas.renderDebugString("%7d TPS (%3d ms)".formatted(tps, lastTickDurationMillis), Color.GRAY, canvas.getWidth() - 250, 34);

        long renderTimeMillis = System.currentTimeMillis() - lastRenderTimeMillis;
        int fps = renderTimeMillis == 0 ? FPS : (int) (1000 / renderTimeMillis);
        canvas.renderDebugString("%7d FPS (%3d ms)".formatted(fps, renderTimeMillis), Color.GRAY, canvas.getWidth() - 250, 68);
    }

    public void toggleDebug() {
        canvas.setDebug(!canvas.isDebug());
    }

    private Game game() {
        return app.getGame();
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        super.processMouseMotionEvent(e);
        mouseX = e.getX();
        mouseY = e.getY();
    }

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

    public int getMouseX() {
        return mouseX;
    }
    public int getMouseY() {
        return mouseY;
    }
    public boolean isMouseDown() {
        return mouseDown;
    }

    public record Settings(
            Dimension size,
            Color backgroundColor
    ) {}
}
