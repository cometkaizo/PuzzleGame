package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Text;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Screen overlay for a book
 */
public class BookOverlay extends Overlay {
    private static final Content[] CONTENT = {
            new Content("""
                    Left page""", """
                    Right page"""),
    };
    private final int variant;
    private final Content content;

    public BookOverlay(GameApp app, int variant) {
        super(app);
        this.variant = variant;
        content = CONTENT[variant];
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture("gui/book/" + variant));

        // render content
        content.leftPage.render(canvas);
        content.rightPage.render(canvas);
        for (var text : content.other) text.render(canvas);
    }

    public record Content(Text leftPage, Text rightPage, Text... other) {
        public Content(String leftPage, String rightPage, Text... other) {
            this(
                    new Text(leftPage, Assets.font(20), Color.BLACK,
                            w -> w / 2 - 100, h -> h / 2 - 60, 94, false, false),
                    new Text(rightPage, Assets.font(20), Color.BLACK,
                            w -> w / 2 + 6, h -> h / 2 - 60, 94, false, false),
                    other
            );
        }
    }
}
