package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;

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

    public TheThinkerOverlay(GameApp app) {
        super(app);
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture("gui/sculpture/thinker/regular"));

        for (StickyNote note : notes) note.render(canvas);
    }
}
