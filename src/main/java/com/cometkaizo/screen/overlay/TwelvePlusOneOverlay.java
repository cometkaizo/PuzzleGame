package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.ImageClickable;

import java.util.Set;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-12
 * Description: Screen overlay for the twelve plus one puzzle
 */
public class TwelvePlusOneOverlay extends Overlay {
    private final Set<Letter> letters = Set.of(
            new Letter("T", 0),
            new Letter("W", 1),
            new Letter("V", 2),
            new Letter("E", 3),
            new Letter("L", 4),
            new Letter("L", 5),
            new Letter("U", 6),
            new Letter("P", 7),
            new Letter("E", 8),
            new Letter("S", 9),
            new Letter("N", 10),
            new Letter("E", 11),
            new Letter("O", 12)
    );
    private Letter selected;

    /// Creates a new overlay
    public TwelvePlusOneOverlay(GameApp app) {
        super(app);
    }

    /// Renders the overlay to the screen
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture("gui/twelve_plus_one/regular"));

        for (var l : letters) l.render(canvas);
    }

    /// A single letter on the overlay
    public class Letter extends ImageClickable {
        public final String letter;
        public int index;
        /// Creates a new letter at the given index
        public Letter(String letter, int index) {
            super(TwelvePlusOneOverlay.this.app, () -> {}, null, null, _ -> 16, _ -> 16, () -> "gui/twelve_plus_one/letter/" + letter, -2, -2);
            this.x = w -> w/2 - 100 + this.index * 18;
            this.y = h -> h/2 - 8 + (this == selected ? -6 : 0);
            this.action = () -> {
                selectOrSwap();
                return true;
            };
            this.letter = letter;
            this.index = index;
        }

        /// Called when this letter is clicked.
        /// If no letter is currently selected, selects this letter.
        /// Otherwise, swaps the selected letter with this letter.
        public void selectOrSwap() {
            if (selected == null) {
                selected = this; // select this
            } else {
                // swap this with selected
                int otherIndex = selected.index;
                selected.index = index;
                index = otherIndex;

                selected = null;
            }
        }

        /// Returns whether this letter is outlined
        @Override
        protected boolean isOutlined() {
            return super.isOutlined() || selected == this;
        }
    }

    /// Ticks this overlay
    @Override
    public void tick() {
        super.tick();
        for (var l : letters) l.tick();
    }

    /// Called when the mouse is pressed
    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        for (var l : letters) {
            if (l.onClick(click)) return;
        }
        selected = null;
    }
}
