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
 * Date Modified: TODO
 * Description: Screen overlay for the thinker
 */
public class TheThinkerOverlay extends Overlay {
    private final List<StickyNote> notes = List.of(
            new StickyNote("w", "right", w -> w/2 + 30, h -> h/2 - 80),
            new StickyNote("i", "right", w -> w/2 + 26, h -> h/2 - 60),
            new StickyNote("ll", "right", w -> w/2 + 32, h -> h/2 + 5),
            new StickyNote("a", "left", w -> w/2 - 38, h -> h/2 - 49),
            new StickyNote("a", "left", w -> w/2 - 38, h -> h/2 - 49),
            new StickyNote("g", "left", w -> w/2 - 20, h -> h/2 + 20),
            new StickyNote("p", "left", w -> w/2 - 20, h -> h/2 - 20),
            new StickyNote("b", "left", w -> w/2 - 30, h -> h/2 + 2),
            new StickyNote("ff", "right", w -> w/2 - 30, h -> h/2 + 70),
            new StickyNote("e", "right", w -> w/2 - 10, h -> h/2 - 53),
            new StickyNote("u", "left", w -> w/2 - 47, h -> h/2 - 30)
    );
    private boolean brainSolved;
    private final Clickable brain = new ImageClickable(app, () -> {
        if (!brainSolved) app.setOverlay(new BrainOverlay(app, this::solveBrain, this));
    }, w -> w/2 + 12, h -> h/2 - 89, _ -> 32, _ -> 24, () -> "gui/sculpture/thinker/brain", -2, -2);

    public TheThinkerOverlay(GameApp app) {
        super(app);
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture("gui/sculpture/thinker/regular"));

        brain.render(canvas);
        for (StickyNote note : notes) note.render(canvas);
    }

    @Override
    public void tick() {
        super.tick();
        brain.tick();
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        brain.onClick(click);
    }

    private void solveBrain() {
        brainSolved = true;
        app.narrate("The immense brainpower produces what looks like a part of a machine. You take it.", this);
        app.getGame().getInventory().add(new MachinePieceItem());
    }
}
