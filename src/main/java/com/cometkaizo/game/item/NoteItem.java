package com.cometkaizo.game.item;

import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Renderable;
import com.cometkaizo.screen.Text;

import java.awt.*;

public class NoteItem extends Item implements Renderable {
    private static final String[] MESSAGES = {
            "NOTE 1",
            "\nNOTE 2",
            "\n\nNOTE 3",
            "\n\n\nNOTE 4",
    };
    public final String message;
    private final Text text;
    public NoteItem(int variant) {
        this.message = MESSAGES[variant];
        text = new Text(message, Assets.font("BoldPixels", 20), Color.BLACK, w -> w/2 - 48, h -> h/2 - 30, 94, false, false);
    }

    @Override
    protected String getTexturePathImpl() {
        return "note";
    }

    @Override
    public String getName() {
        return "Note";
    }

    @Override
    public void render(Canvas canvas) {
        canvas.renderCenteredImage(Assets.texture("gui/note_small"));
        text.render(canvas);
    }
}
