package com.cometkaizo.game.item;

import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Renderable;
import com.cometkaizo.screen.Text;
import com.cometkaizo.world.Args;

import java.awt.*;

public class NoteItem extends Item implements Renderable {
    private static final String[] MESSAGES = {
            """
            The       is 4
            
            
            The          wheel is                   7 times.
            
            
            The                  is decremented   times.
            
            
            The large right
            
            
            
            """,
            """
            The day                       Zip
            
            
            The                   is
            
            
            The outer wheel is                  4 times.
            
            
            The                                     incremented thrice.
            
            
            
            """,
            """
            The day is               5
            
            
            The inner wheel is incremented 7 times.
            
            
            The                   is
            
            
            The                 most          is                       ice
            
            
            
            """,
            """
            The day is     Akbal
            
            
            The                   is
            
            
            The                   is                    times.
            
            
            The                        wheel is
            
            
            Finally, three days pass.
            """,
    };
    public final String message;
    private final int variant;
    private final Text text;
    public NoteItem(Args args) {
        this(args.nextInt(0));
    }
    public NoteItem(int variant) {
        this.message = MESSAGES[variant];
        this.variant = variant;
        text = new Text(message, Assets.font("BoldPixels", 20), Color.BLACK, w -> w/2 - 52, h -> h/2 - 50, 120, false, false);
    }

    @Override
    protected String getNamespace() {
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

    @Override
    public String write() {
        return new Args(getNamespace(), variant+"").toString();
    }
}
