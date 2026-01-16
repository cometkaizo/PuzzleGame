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
            new Content("", "",
                    new Label("China", 49, -4),
                    new Label("Russia", 42, -30),
                    new Label("India", 36, 4),
                    new Label("Japan", 71, -11),
                    new Label("Kanagawa", 71, -6, 15),
                    new Label("Canada", -63, -27),
                    new Label("United States", -65, -7),
                    new Label("Brazil", -38, 24),
                    new Label("Greenland", -25, -41),
                    new Label("Australia", 69, 34),
                    new Label("France", -6, -11),
                    new Label("England", -10, -18),
                    new Label("Antarctica", 32, 57)
                    ),
            new Content("", "Organs in the Human Body\n\n\n1 - Brain\n\n2 - Mouth\n\n3 - Esophagus\n\n4 - Lungs\n\n5 - Heart\n\n6 - Liver\n\n7 - Stomach\n\n8 - Small Intestine\n\n9 - Large Intestine",
                    new Label("1", -51, -48),
                    new Label("2", -39, -35),
                    new Label("3", -52, -28),
                    new Label("4", -60, -8),
                    new Label("4", -43, -8),
                    new Label("5", -51, -7),
                    new Label("6", -60, 10),
                    new Label("7", -46, 13),
                    new Label("8", -51, 28),
                    new Label("9", -39, 27)),
            // ÑŽÇäëØü
            new Content("""
                    DICTIONARY of the Symbolic Language
                    
                    
                    ÑŽØŽÇ
                       to trudge through anything in which
                       the feet sink
                    
                    ÑŽØÇë
                       to blend together; to adapt
                    
                    ÑŽüØüü
                       corner, edge or slice of anything
                    
                    ÑäØŽ
                       of, like or pertaining to a sheriff
                    
                    ÑØŽØ
                       spar forming an extension of the
                       bowsprit
                    
                    ŽÑüäØ
                       obstinate quirk or habit
                    """, """
                    (cont.)
                    
                    
                    ŽÑüëü
                       belief that Christ is everywhere
                    
                    ŽäØÑÑ
                       animal crossbred from male yak and
                       domestic cow
                    
                    ëëØ
                       rhetorical device of repeating
                       conjunction for emphasis
                    
                    ëØÇØ
                       instrument for viewing the colon
                    
                    ëØäü
                       belief that knowledge is always probable
                       but never absolute
                    
                    ëØüä
                       capable of being sold""",
                    new Text("252", Assets.font(15), Color.BLACK, w -> w/2 - 104, h -> h/2 + 55, 100, false, false),
                    new Text("253", Assets.font(15), Color.BLACK, w -> w/2 + 97, h -> h/2 + 55, 100, false, false)),
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

    public static class Label extends Text {
        public Label(String message, int dx, int dy) {
            this(message, dx, dy, 24);
        }
        public Label(String message, int dx, int dy, int size) {
            super(message, Assets.font(size), Color.BLACK, w -> w/2 + dx, h -> h/2 + dy, 100, true, true);
        }
    }
}
