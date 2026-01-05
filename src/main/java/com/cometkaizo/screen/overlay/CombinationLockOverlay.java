package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Clickable;
import com.cometkaizo.screen.ImageClickable;

import java.awt.*;
import java.util.List;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Screen overlay for combination lock
 */
public class CombinationLockOverlay extends Overlay {
    private final Font font = Assets.font("BoldPixels", 50);
    private final Color color = Color.BLACK;
    private final String correctCombination;
    private final String[] digitOptions;
    private final Runnable actionOnOpen;
    private final int[] selectedDigits = new int[4];
    private boolean open;
    private final String overlayVariant;

    private final Clickable handleClickable;
    private final List<Clickable> digitClickables;

    public CombinationLockOverlay(GameApp app, String correctCombination, String[] digitOptions, Runnable actionOnOpen, String overlayVariant) {
        super(app);
        this.correctCombination = correctCombination;
        this.digitOptions = digitOptions;
        this.actionOnOpen = actionOnOpen;
        this.overlayVariant = overlayVariant;

        handleClickable = new ImageClickable(this.app, this::open,
                w -> w / 2 - 38, h -> h / 2 - 64 - getYOffset(), _ -> 76, _ -> 32,
                () -> "gui/combination_lock/" + this.overlayVariant + "/pull", -4, -4);
        digitClickables = List.of(
                newDigitClickable(0),
                newDigitClickable(1),
                newDigitClickable(2),
                newDigitClickable(3)
        );
    }
    private Clickable newDigitClickable(int id) {
        return new ImageClickable(app, () -> changeDigit(id),
                w -> w / 2 + 18, h -> h / 2 - 16 + id * 18 + getYOffset(),
                _ -> 38, _ -> 16, () -> "gui/combination_lock/" + overlayVariant + "/digit", -4, -4);
    }

    private void open() {
        if (currentCombination().equals(correctCombination)) {
            open = true;
            actionOnOpen.run();
        }
    }

    private void changeDigit(int digitId) {
        if (open) return;
        selectedDigits[digitId] ++;
        selectedDigits[digitId] %= digitOptions[digitId].length();
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        handleClickable.render(canvas);
        canvas.renderImage(Assets.texture("gui/combination_lock/" + overlayVariant + "/body"), canvas.halfWidth(), canvas.halfHeight() + canvas.scale(getYOffset()), -0.5, -0.5);

        digitClickables.forEach(c -> c.render(canvas));
        renderCurrentCombination(canvas);
    }

    private void renderCurrentCombination(Canvas canvas) {
        String currentCombo = currentCombination();
        int yOffset = -5 + getYOffset();
        for (int i = 0; i < 4; i ++) {
            int x = canvas.halfWidth() + canvas.scale(34);
            int y = canvas.halfHeight() + canvas.scale(yOffset + i*18);
            canvas.renderString("" + currentCombo.charAt(i), font, color, x, y, false, false);
        }
    }
    private int getYOffset() {
        return open ? 28 : 0;
    }

    private String currentCombination() {
        return "" + digitOptions[0].charAt(selectedDigits[0])
                + digitOptions[1].charAt(selectedDigits[1])
                + digitOptions[2].charAt(selectedDigits[2])
                + digitOptions[3].charAt(selectedDigits[3]);
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        handleClickable.onClick(click);
        digitClickables.forEach(c -> c.onClick(click));
    }

    @Override
    public void tick() {
        handleClickable.tick();
        digitClickables.forEach(Clickable::tick);
    }
}
