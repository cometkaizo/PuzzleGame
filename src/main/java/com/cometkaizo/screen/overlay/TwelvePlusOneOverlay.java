package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Clickable;
import com.cometkaizo.screen.ImageClickable;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-12
 * Description: Screen overlay for the twelve plus one puzzle
 */
public class TwelvePlusOneOverlay extends Overlay {
    private final Set<Letter> letters = new HashSet<>();
    private Letter selected;
    private final Clickable resetButton = new ImageClickable(app, this::resetLetters, w -> w/2, h -> h/2 + 40, _ -> 23, _ -> 23, () -> "gui/chess/reset_button", -2, -2);

    /// Creates a new overlay
    public TwelvePlusOneOverlay(GameApp app) {
        super(app);
        resetLetters();
    }

    /// Resets the letters to the original layout
    protected void resetLetters() {
        selected = null;

        letters.clear();
        letters.add(new Letter("T", 0));
        letters.add(new Letter("W", 1));
        letters.add(new Letter("V", 2));
        letters.add(new Letter("E", 3));
        letters.add(new Letter("L", 4));
        letters.add(new Letter("L", 5));
        letters.add(new Letter("U", 6));
        letters.add(new Letter("P", 7));
        letters.add(new Letter("E", 8));
        letters.add(new Letter("S", 9));
        letters.add(new Letter("N", 10));
        letters.add(new Letter("E", 11));
        letters.add(new Letter("O", 12));
    }

    /// Renders the overlay to the screen
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture("gui/twelve_plus_one/regular"));

        for (var l : letters) l.render(canvas);

        resetButton.render(canvas);
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
        resetButton.tick();
    }

    /// Called when the mouse is pressed
    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        if (resetButton.onClick(click)) return;
        for (var l : letters) {
            if (l.onClick(click)) return;
        }
        selected = null;
    }
}
