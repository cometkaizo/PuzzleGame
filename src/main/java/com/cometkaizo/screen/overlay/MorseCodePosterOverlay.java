package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.ImageClickable;
import com.cometkaizo.screen.Text;

import java.awt.*;
import java.util.function.BooleanSupplier;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Screen overlay for the morse code poster
 */
public class MorseCodePosterOverlay extends Overlay {
    private static final String[] SYMBOLS = {"Ñ", "Ž", "Ç", "ä", "ë", "Ø", "ü", };
    private final Text title, desc;
    private final BooleanSupplier isLit;
    private final NoteSlot[] slots = {
            new NoteSlot(-1, -60, -53),
            new NoteSlot(-1, -60, -35),
            new NoteSlot(-1, -60, -17),
            new NoteSlot(-1, -60, 1),
            new NoteSlot(-1, -60, 19),
            new NoteSlot(-1, -60, 37),
            new NoteSlot(-1, -60, 55),
            new NoteSlot(0, -1, -48),
            new NoteSlot(1, 40, -46),
            new NoteSlot(2, 10, 5),
            new NoteSlot(3, 1, -19),
            new NoteSlot(4, 18, -47),
            new NoteSlot(5, 41, -4),
            new NoteSlot(6, 35, -23),
    };
    private NoteSlot selected;

    public MorseCodePosterOverlay(GameApp app, BooleanSupplier isLit) {
        super(app);
        this.isLit = isLit;
        title = new Text("""
                Morse Code Translation""", Assets.font(40), Color.BLACK,
                w -> w / 2 - 58,
                h -> h / 2 - 82,
                200, false, false);
        desc = new Text("""
                Each letter from the ÇäÑÑŽ language, in ALPHABETICAL ORDER from top to bottom, is paired with a dot-dash combo.""", Assets.font(20), Color.BLACK,
                w -> w / 2 - 58,
                h -> h / 2 - 72,
                116, false, false);
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture("gui/morse_code_poster/regular"));

        // render the light if the poster is currently lit
        if (isLit.getAsBoolean()) {
            int x = canvas.scale(canvas.halfPixelWidth() + 7);
            int y = canvas.scale(canvas.halfPixelHeight() + 23);
            canvas.renderImage(Assets.texture("gui/morse_code_poster/light"), x, y);
        }

        title.render(canvas);
        desc.render(canvas);

        for (var slot : slots) slot.render(canvas);
    }

    public class NoteSlot extends ImageClickable {
        public int id;

        public NoteSlot(int id, int dx, int dy) {
            super(MorseCodePosterOverlay.this.app, () -> {}, w -> w/2 + dx, null, _ -> 16, _ -> 16, null, -2, -2);
            this.y = h -> h/2 + dy + (this == selected ? -4 : 0);
            this.action = this::selectOrSwap;
            this.texturePath = () -> "gui/morse_code_poster/" + (this.id == -1 ? "note_empty" : "note");
            this.id = id;
        }

        private boolean selectOrSwap() {
            if (selected == null) {
                if (id != -1) selected = this;
            } else {
                int otherId = selected.id;
                selected.id = this.id;
                this.id = otherId;
                selected = null;
            }
            return true;
        }

        @Override
        protected boolean isOutlined() {
            return super.isOutlined() || selected == this;
        }

        @Override
        public void render(Canvas canvas) {
            super.render(canvas);
            if (id != -1) canvas.renderString(SYMBOLS[id], Assets.font(50), new Color(90, 130, 132), lastX + canvas.scale(8), lastY + canvas.scale(11), true, false);
        }
    }

    @Override
    public void tick() {
        super.tick();
        for (var slot : slots) slot.tick();
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        for (var slot : slots) slot.onClick(click);
    }
}
