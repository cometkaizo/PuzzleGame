package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Button;
import com.cometkaizo.screen.Canvas;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.util.List;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: Screen overlay for the title screen
 */
public class TitleScreen extends Overlay {
    private static final Font TITLE_FONT = Assets.font("BoldPixels").deriveFont(Font.PLAIN, 70);
    private static final String TITLE = "Puzzle Game";
    private final List<Button> buttons = List.of(
            new Button(app, "New Game", 30, this::newGame, w -> w / 2 - 28, h -> h / 2 - 24, _ -> 58, _ -> 16),
            new Button(app, "Load Game", 30, this::loadGame, w -> w / 2 - 28, h -> h / 2, _ -> 58, _ -> 16),
            new Button(app, "Debug Game", 30, this::debugGame, w -> w / 2 - 28, h -> h / 2 + 24, _ -> 58, _ -> 16)
    );
    private Clip music;

    /// Creates a new overlay
    public TitleScreen(GameApp app) {
        super(app);
    }

    @Override
    public void setup() {
        super.setup();
        music = Assets.sound("title_screen_music").play();
        music.loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        if (music != null) music.stop();
    }

    // button functionality
    /// Starts a new game
    private void newGame() {
        app.setOverlay(null);
    }
    /// Loads a game from the save slot
    private void loadGame() {
        if (app.loadGame()) {
            app.setOverlay(null);
        }
    }
    /// Loads a game from the save slot in debug mode
    private void debugGame() {
        if (app.loadGame()) {
            app.getGame().setDevMode(true);
            app.toggleDebug();
            app.setOverlay(null);
        }
    }

    /// Called when the mouse is pressed
    @Override
    protected void onClick(MousePressedEvent click) {
        for (var button : buttons) button.onClick(click);
    }

    /// Ticks this overlay
    @Override
    public void tick() {
        buttons.forEach(com.cometkaizo.screen.Button::tick);
    }

    /// Renders this overlay to the screen
    @Override
    public void render(Canvas canvas) {
        canvas.fillScreen(Color.BLACK);

        canvas.renderString(TITLE, TITLE_FONT, Color.WHITE, canvas.getWidth() / 2F, canvas.getHeight() / 2F - 200, true, true);

        buttons.forEach(button -> button.render(canvas));
    }

    /// Returns whether the game should be updated while this screen is visible
    @Override
    public boolean shouldTickGame() {
        return false;
    }

    /// Returns whether the game should be rendered while this screen is visible
    @Override
    public boolean shouldRenderGame() {
        return false;
    }
}
