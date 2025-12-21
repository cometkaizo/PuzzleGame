package com.cometkaizo.screen;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;

import java.awt.*;
import java.util.List;

public class TitleScreen implements Overlay {
    private final GameApp app;
    private static final Font TITLE_FONT = Assets.font("BoldPixels").deriveFont(Font.PLAIN, 70);
    private static final String TITLE = "Puzzle Game";
    private final List<Button> buttons = List.of(
            new Button("New Game", 30, this::newGame, w -> w / 2 - 100, h -> h / 2 - 100, _ -> 200, _ -> 50),
            new Button("Load Game", 30, this::loadGame, w -> w / 2 - 100, h -> h / 2 - 40, _ -> 200, _ -> 50)
    );

    public TitleScreen(GameApp app) {
        this.app = app;
        app.getGame().getEventBus().register(MousePressedEvent.class, this::onClick);
    }
    @Override
    public void cleanup() {
        app.getGame().getEventBus().unregister(MousePressedEvent.class, this::onClick);
    }

    // button functionality
    private boolean newGame() {
        app.setOverlay(null);
        return true;
    }
    private boolean loadGame() {
        if (app.loadGame()) {
            app.setOverlay(null);
            return true;
        } else return false;
    }

    private void onClick(MousePressedEvent click) {
        for (var button : buttons) {
            if (button.onClick(click)) break;
        }
    }

    @Override
    public void tick() {
        buttons.forEach(Button::tick);
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
