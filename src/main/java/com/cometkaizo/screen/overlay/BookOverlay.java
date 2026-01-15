package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Renderable;
import com.cometkaizo.screen.Text;

import java.awt.*;
import java.util.function.IntUnaryOperator;

import static com.cometkaizo.world.entity.MayanCalendar.*;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Screen overlay for a book
 */
public class BookOverlay extends Overlay {
    private static final Content[] CONTENT = {
            new Content("""
                    The Mayan Calendar is an ancient system of keeping track of time. The Mayans identified each day with four digits.
                    
                    Digit 1 = number between 0 - 12
                    Digit 2 = 20 spirit names
                    Digit 3 = number between 0 - 19
                    Digit 4 = 19 month names
                    
                    Mayan mathematics
                    """, """
                    Tzolkin translation       Haab translation""",
                    new MayanSymbol(true, 0),
                    new MayanSymbol(true, 1),
                    new MayanSymbol(true, 2),
                    new MayanSymbol(true, 3),
                    new MayanSymbol(true, 4),
                    new MayanSymbol(true, 5),
                    new MayanSymbol(true, 6),
                    new MayanSymbol(true, 7),
                    new MayanSymbol(true, 8),
                    new MayanSymbol(true, 9),
                    new MayanSymbol(true, 10),
                    new MayanSymbol(true, 11),
                    new MayanSymbol(true, 12),
                    new MayanSymbol(true, 13),
                    new MayanSymbol(true, 14),
                    new MayanSymbol(true, 15),
                    new MayanSymbol(true, 16),
                    new MayanSymbol(true, 17),
                    new MayanSymbol(true, 18),
                    new MayanSymbol(true, 19),
                    new MayanSymbol(false, 0),
                    new MayanSymbol(false, 1),
                    new MayanSymbol(false, 2),
                    new MayanSymbol(false, 3),
                    new MayanSymbol(false, 4),
                    new MayanSymbol(false, 5),
                    new MayanSymbol(false, 6),
                    new MayanSymbol(false, 7),
                    new MayanSymbol(false, 8),
                    new MayanSymbol(false, 9),
                    new MayanSymbol(false, 10),
                    new MayanSymbol(false, 11),
                    new MayanSymbol(false, 12),
                    new MayanSymbol(false, 13),
                    new MayanSymbol(false, 14),
                    new MayanSymbol(false, 15),
                    new MayanSymbol(false, 16),
                    new MayanSymbol(false, 17),
                    new MayanSymbol(false, 18),
                    new MayanNumber(0),
                    new MayanNumber(1),
                    new MayanNumber(2),
                    new MayanNumber(3),
                    new MayanNumber(4),
                    new MayanNumber(5),
                    new MayanNumber(6),
                    new MayanNumber(7),
                    new MayanNumber(8),
                    new MayanNumber(9),
                    new MayanNumber(10),
                    new MayanNumber(11)
            ),
            new Content("", ""),
            new Content("", ""),
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
        canvas.renderCenteredImage(Assets.texture("gui/book/" + (variant + 1)));

        // render content
        content.leftPage.render(canvas);
        content.rightPage.render(canvas);
        for (var text : content.other) text.render(canvas);
    }

    public record Content(Text leftPage, Text rightPage, Renderable... other) {
        public Content(String leftPage, String rightPage, Renderable... other) {
            this(
                    new Text(leftPage, Assets.font(20), Color.BLACK,
                            w -> w / 2 - 104, h -> h / 2 - 60, 94, false, false),
                    new Text(rightPage, Assets.font(20), Color.BLACK,
                            w -> w / 2 + 10, h -> h / 2 - 60, 94, false, false),
                    other
            );
        }
    }

    public record MayanSymbol(Image image, IntUnaryOperator x, IntUnaryOperator y, Text text) implements Renderable {
        public MayanSymbol(boolean tzolkin, int id) {
            int maxSymbolsPerCol = 11;
            this(tzolkin ? "tzolkin_symbol/" + id : "haab_symbol/" + id, (tzolkin ? 6 : 57) + (id / maxSymbolsPerCol) * 26, -50 + (id % maxSymbolsPerCol) * 10,
                    tzolkin ? TZOLKIN_ENGLISH_NAMES[id]+"\n"+TZOLKIN_NAMES[id] : HAAB_ENGLISH_NAMES[id]+"\n"+HAAB_NAMES[id]);
        }
        public MayanSymbol(String name, int dx, int dy, String text) {
            this(Assets.texture("gui/mayan_calendar/" + name), w -> w/2 + dx, h -> h/2 + dy,
                    new Text(text, Assets.font(16), Color.BLACK, w -> w/2 + dx + 9, h -> h/2 + dy - 1, 100, false, false));
        }
        @Override
        public void render(Canvas canvas) {
            int x = canvas.scale(this.x.applyAsInt(canvas.getPixelWidth()));
            int y = canvas.scale(this.y.applyAsInt(canvas.getPixelHeight()));
            var g = canvas.getGraphics();
            int symbolSize = canvas.scale(8);

            g.drawImage(image, x, y, symbolSize, symbolSize, null);
            text.render(canvas);
        }
    }
    public record MayanNumber(Image image, IntUnaryOperator x, IntUnaryOperator y, Text text) implements Renderable {
        public MayanNumber(int num) {
            int maxNumsPerCol = 6;
            int dx = -104 + num / maxNumsPerCol * 20;
            int dy = -4 + num % maxNumsPerCol * 10;
            this(Assets.texture("gui/mayan_calendar/number/" + num), w -> w/2 + dx, h -> h/2 + dy,
                    new Text("= " + num, Assets.font(16), Color.BLACK, w -> w/2 + dx + 9, h -> h/2 + dy + 1, 100, false, false));
        }
        @Override
        public void render(Canvas canvas) {
            int x = canvas.scale(this.x.applyAsInt(canvas.getPixelWidth()));
            int y = canvas.scale(this.y.applyAsInt(canvas.getPixelHeight()));
            var g = canvas.getGraphics();
            int symbolSize = canvas.scale(8);

            g.drawImage(image, x, y, symbolSize, symbolSize, null);
            text.render(canvas);
        }
    }
}
