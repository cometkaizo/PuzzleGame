package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Button;
import com.cometkaizo.screen.Canvas;

import java.awt.*;
import java.util.List;

public class TitleScreen extends Overlay {
    private static final Font TITLE_FONT = Assets.font("BoldPixels").deriveFont(Font.PLAIN, 70);
    private static final String TITLE = "Puzzle Game";
    private final List<Button> buttons = List.of(
            new Button(app, "New Game", 30, this::newGame, w -> w / 2 - 24, h -> h / 2 - 24, _ -> 50, _ -> 12),
            new Button(app, "Load Game", 30, this::loadGame, w -> w / 2 - 24, h -> h / 2 - 10, _ -> 50, _ -> 12)
    );

    public TitleScreen(GameApp app) {
        super(app);
    }

    // button functionality
    private void newGame() {
        app.setOverlay(null);
    }
    private void loadGame() {
        if (app.loadGame()) {
            app.setOverlay(null);
        }
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        for (var button : buttons) button.onClick(click);
    }

    @Override
    public void tick() {
        buttons.forEach(com.cometkaizo.screen.Button::tick);
    }

    @Override
    public void render(Canvas canvas) {
        canvas.renderString(TITLE, TITLE_FONT, Color.BLACK, canvas.getWidth() / 2F, canvas.getHeight() / 2F - 200, true, true);

        buttons.forEach(button -> button.render(canvas));
    }

    @Override
    public boolean shouldTickGame() {
        return false;
    }

    @Override
    public boolean shouldRenderGame() {
        return false;
    }
}
