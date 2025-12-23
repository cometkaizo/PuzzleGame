package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Clickable;

import java.awt.*;
import java.util.List;

public class CombinationLockOverlay extends Overlay {
    private final Font font = Assets.font("BoldPixels", 50);
    private final Color color = Color.BLACK;
    private final String correctCombination;
    private final String[] digitOptions;
    private final Runnable actionOnOpen;
    private final int[] selectedDigits = new int[4];
    private boolean open;

    private final List<Clickable> clickables = List.of(
            new Clickable(this::open, w -> w / 2 - 38, h -> h / 2 - 64, _ -> 76, _ -> 32),
            newDigitClickable(0),
            newDigitClickable(1),
            newDigitClickable(2),
            newDigitClickable(3)
    );

    private Clickable newDigitClickable(int id) {
        return new Clickable(() -> changeDigit(id), w -> w / 2 + 18, h -> h / 2 - 16 + id * 18, _ -> 38, _ -> 16);
    }

    public CombinationLockOverlay(GameApp app, String correctCombination, String[] digitOptions, Runnable actionOnOpen) {
        super(app);
        this.correctCombination = correctCombination;
        this.digitOptions = digitOptions;
        this.actionOnOpen = actionOnOpen;
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
        canvas.renderImage(getTexture(), canvas.halfWidth(), canvas.halfHeight(), -0.5, -0.5);

        clickables.forEach(c -> c.render(canvas));
        renderCurrentCombination(canvas);
    }

    private void renderCurrentCombination(Canvas canvas) {
        String currentCombo = currentCombination();
        double yOffset = -5 + (open ? 28 : 0);
        double scale = canvas.renderScale();
        for (int i = 0; i < 4; i ++) {
            int x = (int) (canvas.halfWidth() + 34 * scale);
            int y = (int) (canvas.halfHeight() + yOffset*scale + (i*18) * scale);
            canvas.renderString("" + currentCombo.charAt(i), font, color, x, y, false, false);
        }
    }

    private Image getTexture() {
        // get the texture variant depending on which interactable area is hovered over
        String variant;
        if (open) variant = "open";
        else if (clickables.get(0).contains(mouseX(), mouseY())) variant = "pull";
        else if (clickables.get(1).contains(mouseX(), mouseY())) variant = "1";
        else if (clickables.get(2).contains(mouseX(), mouseY())) variant = "2";
        else if (clickables.get(3).contains(mouseX(), mouseY())) variant = "3";
        else if (clickables.get(4).contains(mouseX(), mouseY())) variant = "4";
        else variant = "0"; // no area is hovered over

        return Assets.texture("gui/combination_lock/" + variant);
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
        clickables.forEach(c -> c.onClick(click));
    }

    @Override
    public void tick() {
        clickables.forEach(Clickable::tick);
    }
}
