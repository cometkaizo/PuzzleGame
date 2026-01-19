package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Clickable;
import com.cometkaizo.screen.ImageClickable;

import java.util.Arrays;
import java.util.List;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-08
 * Description: Screen overlay for the lever lock
 */
public class LeverLockOverlay extends Overlay {
    private final boolean[][] correctCombination;
    private final Runnable actionOnOpen;
    private final boolean[][] currentCombination = new boolean[2][7];
    private boolean open;

    private final Clickable submitClickable;
    private final List<Clickable> leverClickables;

    public LeverLockOverlay(GameApp app, boolean[][] correctCombination, Runnable actionOnOpen, Overlay next) {
        super(app, next);
        this.correctCombination = correctCombination;
        this.actionOnOpen = actionOnOpen;

        submitClickable = new ImageClickable(this.app, this::open,
                w -> w / 2 - 13, h -> h / 2 + 9, _ -> 26, _ -> 26,
                () -> "gui/lever_lock/submit", -2, -2);
        leverClickables = List.of(
                newLeverClickable(0, 0),
                newLeverClickable(0, 1),
                newLeverClickable(0, 2),
                newLeverClickable(0, 3),
                newLeverClickable(0, 4),
                newLeverClickable(0, 5),
                newLeverClickable(0, 6),
                newLeverClickable(1, 0),
                newLeverClickable(1, 1),
                newLeverClickable(1, 2),
                newLeverClickable(1, 3),
                newLeverClickable(1, 4),
                newLeverClickable(1, 5),
                newLeverClickable(1, 6)
        );
    }
    private Clickable newLeverClickable(int r, int c) {
        return new ImageClickable(app, () -> flipLever(r, c),
                w -> w / 2 - 62 + c * 18, h -> h / 2 - 30 + r * 18,
                _ -> 16, _ -> 16,
                () -> "gui/lever_lock/light_" + (currentCombination[r][c] ? "on" : "off"), -2, -2);
    }

    private void open() {
        if (open) return;
        if (Arrays.deepEquals(currentCombination, correctCombination)) {
            open = true;
            actionOnOpen.run();
        } else {
            app.narrate("Nothing happens.", this);
            Assets.sound("wrong").play();
        }
    }

    private void flipLever(int r, int c) {
        if (open) return;
        currentCombination[r][c] = !currentCombination[r][c];
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderImage(Assets.texture("gui/lever_lock/body"), canvas.halfWidth(), canvas.halfHeight(), -0.5, -0.5);

        submitClickable.render(canvas);
        leverClickables.forEach(c -> c.render(canvas));
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        submitClickable.onClick(click);
        leverClickables.forEach(c -> c.onClick(click));
    }

    @Override
    public void tick() {
        submitClickable.tick();
        leverClickables.forEach(Clickable::tick);
    }
}
