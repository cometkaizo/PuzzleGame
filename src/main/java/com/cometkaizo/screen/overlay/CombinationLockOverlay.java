package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Clickable;

import java.awt.*;
import java.util.List;

public class CombinationLockOverlay extends Overlay {
    private final String correctCombination;
    private final String[] digitOptions;
    private final List<Clickable> clickables = List.of(
//            new Clickable(this::newGame, w -> w / 2 - 100, h -> h / 2 - 100, _ -> 200, _ -> 50),
//            new Clickable(this::loadGame, w -> w / 2 - 100, h -> h / 2 - 40, _ -> 200, _ -> 50)
    );

    public CombinationLockOverlay(GameApp app, String correctCombination, String[] digitOptions) {
        super(app);
        this.correctCombination = correctCombination;
        this.digitOptions = digitOptions;
    }

    @Override
    public void render(Canvas canvas) {
        canvas.fillScreen(new Color(0, 0, 0, 200));
        canvas.renderImage(getTexture(), canvas.halfWidth(), canvas.halfHeight(), -0.5, -0.5);
    }

    private static Image getTexture() {
        return Assets.texture("gui/combination_lock");
    }

    @Override
    public void tick() {
        clickables.forEach(Clickable::tick);
    }
}
