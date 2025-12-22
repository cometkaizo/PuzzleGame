package com.cometkaizo.screen;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.Game;
import com.cometkaizo.game.GameSettings;
import com.cometkaizo.world.Vector;

import javax.swing.*;
import java.awt.*;

public class GameRenderer extends JPanel {

    private final Canvas canvas;
    private final GameApp app;
    private final Dimension size;
    private double partialTick;

    public GameRenderer(Settings settings, GameApp app) {
        this.app = app;

        GameSettings gameSettings = game().getSettings();

        canvas = new Canvas(gameSettings.tileSize,
                game().getCameraPosition().x,
                game().getCameraPosition().y,
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

    public void setPartialTick(double partialTick) {
        this.partialTick = partialTick;
    }

    protected void render(Graphics2D g) {
        Dimension size = getSize(this.size);

        if (app.shouldRenderGame()) renderGame(g, size);
        if (app.shouldTickOrRenderOverlay()) renderTitleScreen(g, size);
    }

    private void renderGame(Graphics2D g, Dimension size) {
        canvas.startRender(g, game().getPrevCameraPosition(), game().getCameraPosition(), size.width, size.height, partialTick);

        game().render(canvas);

        canvas.endRender();
    }

    // todo: change this to overlay system which supports both title screens and pause menus + puzzle interfaces
    private void renderTitleScreen(Graphics2D g, Dimension size) {
        canvas.startRender(g, Vector.immutable(0D, 0D), Vector.immutable(0D, 0D), size.width, size.height, partialTick);

        app.getOverlay().render(canvas);

        canvas.endRender();
    }

    private void renderOverlay(Graphics2D g, Dimension size) {
        // todo
    }

    public void toggleDebug() {
        canvas.setDebug(!canvas.isDebug());
    }

    public record Settings(
            Dimension size,
            Color backgroundColor
    ) {}

    private Game game() {
        return app.getGame();
    }
}
