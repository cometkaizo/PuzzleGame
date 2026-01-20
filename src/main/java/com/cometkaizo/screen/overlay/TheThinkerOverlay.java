package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.game.item.MachinePieceItem;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Clickable;
import com.cometkaizo.screen.ImageClickable;

import java.util.List;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: Screen overlay for the thinker
 */
public class TheThinkerOverlay extends Overlay {
    private final List<RedLabel> notes = List.of(
            new RedLabel("w", w -> w/2 + 30, h -> h/2 - 80),
            new RedLabel("i", w -> w/2 + 26, h -> h/2 - 60),
            new RedLabel("ll", w -> w/2 + 32, h -> h/2 + 5),
            new RedLabel("a", w -> w/2 - 38, h -> h/2 - 49),
            new RedLabel("a", w -> w/2 - 38, h -> h/2 - 49),
            new RedLabel("g", w -> w/2 - 20, h -> h/2 + 20),
            new RedLabel("p", w -> w/2 - 20, h -> h/2 - 20),
            new RedLabel("b", w -> w/2 - 30, h -> h/2 + 2),
            new RedLabel("ff", w -> w/2 - 30, h -> h/2 + 70),
            new RedLabel("e", w -> w/2 - 10, h -> h/2 - 53),
            new RedLabel("u", w -> w/2 - 47, h -> h/2 - 30)
    );
    public boolean brainSolved;
    private final Runnable solveAction;
    private final Clickable brain = new ImageClickable(app, () -> {
        if (!brainSolved) app.setOverlay(new BrainOverlay(app, this::solveBrain, this));
    }, w -> w/2 + 12, h -> h/2 - 89, _ -> 32, _ -> 24, () -> "gui/sculpture/thinker/brain", -2, -2);

    /// Creates a new overlay
    public TheThinkerOverlay(GameApp app, boolean brainSolved, Runnable solveAction) {
        super(app);
        this.brainSolved = brainSolved;
        this.solveAction = solveAction;
    }

    /// Renders this overlay to the screen
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture("gui/sculpture/thinker/regular"));

        brain.render(canvas);
        for (RedLabel note : notes) note.render(canvas);
    }

    /// Ticks this overlay
    @Override
    public void tick() {
        super.tick();
        brain.tick();
    }

    /// Called when the mouse is pressed
    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        brain.onClick(click);
    }

    /// Solves the brain
    private void solveBrain() {
        brainSolved = true;
        solveAction.run();
        app.narrate("The immense brainpower produces what looks like a part of a machine. You take it.", this);
        app.getGame().getInventory().add(new MachinePieceItem());
    }
}
