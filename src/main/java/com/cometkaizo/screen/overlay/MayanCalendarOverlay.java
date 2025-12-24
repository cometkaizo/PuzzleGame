package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Clickable;
import com.cometkaizo.screen.Renderable;
import com.cometkaizo.world.Tickable;

import java.awt.*;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.floorMod;

public class MayanCalendarOverlay extends Overlay {
    public static final Image[] NUMBER_SYMBOLS = IntStream.range(0, 21).mapToObj(i -> Assets.texture("gui/mayan_calendar/number/" + i)).toArray(Image[]::new);
    public static final Image[] TZOLKIN_SYMBOLS = IntStream.range(0, 20).mapToObj(i -> Assets.texture("gui/mayan_calendar/tzolkin_symbol/" + i)).toArray(Image[]::new);
    public static final Image[] HAAB_SYMBOLS = IntStream.range(0, 19).mapToObj(i -> Assets.texture("gui/mayan_calendar/haab_symbol/" + i)).toArray(Image[]::new);
    public static final int NUMBER_SIZE = 10;
    public static final int SYMBOL_SIZE = 16;

    private final Cog big = new BigCog(), med = new MediumCog(), small = new SmallCog();
    private Cog focused = null;
    private final List<Clickable> clickables = List.of(
            new Clickable(this::increment, w -> w / 2 + 62, h -> h / 2 - 50, _ -> 23, _ -> 23),
            new Clickable(this::open, w -> w / 2 + 62, h -> h / 2 - 11, _ -> 23, _ -> 23),
            new Clickable(this::decrement, w -> w / 2 + 62, h -> h / 2 + 26, _ -> 23, _ -> 23),
            new Clickable(() -> focus(small), w -> w / 2 - 58, h -> h / 2 - 27, _ -> 55, _ -> 55),
            new Clickable(() -> focus(med), w -> w / 2 - 119, h -> h / 2 - 67, _ -> 135, _ -> 135),
            new Clickable(() -> focus(big), w -> w / 2 + 20, h -> h / 2 - 75, _ -> 30, _ -> 151)
    );

    public MayanCalendarOverlay(GameApp app) {
        super(app);
    }

    private void increment() {
        if (big.isRotating() || med.isRotating() || small.isRotating()) return;
        if (isFocusing()) focused.increment();
        else {
            big.increment();
            med.increment();
            small.increment();
        }
    }
    private boolean open() {
        if (isFocusing()) return false;

        return true;
    }
    private void decrement() {
        if (big.isRotating() || med.isRotating() || small.isRotating()) return;
        if (isFocusing()) focused.decrement();
        else {
            big.decrement();
            med.decrement();
            small.decrement();
        }
    }

    private boolean focus(Cog cog) {
        if (isFocusing()) return false;
        focused = cog;
        return true;
    }
    private boolean isFocusing() {
        return focused != null;
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        if (focused != big) big.render(canvas);
        if (focused != med) med.render(canvas);
        if (focused != small) small.render(canvas);
        canvas.renderCenteredImage(Assets.texture("gui/mayan_calendar/open_button"));

        if (isFocusing()) {
            canvas.fillScreen(new Color(0, 0, 0, 200));
            focused.render(canvas);
        }

        canvas.renderCenteredImage(Assets.texture("gui/mayan_calendar/rot_buttons"));
        clickables.forEach(c -> c.render(canvas));
    }

    class BigCog extends Cog {
        static final int LENGTH = 365;
        static final int DAYS_PER_MONTH = 20;
        static final Image[] SYMBOLS = IntStream.range(0, LENGTH).mapToObj(i -> HAAB_SYMBOLS[i / 20]).toArray(Image[]::new);

        public BigCog() {
            super("big_cog", LENGTH);
        }
        @Override
        public void render(Canvas canvas) {
            super.render(canvas);
            int yOffset = yOffset(canvas);

            var g = canvas.getGraphics();
            var oldClip = g.getClip();
            g.setClip(canvas.scale(canvas.halfPixelWidth() + 20), canvas.scale(canvas.halfPixelHeight() - 74) + yOffset, canvas.scale(30), canvas.scale(149));

            renderSymbols(canvas);
            renderNumbers(canvas);

            g.setClip(oldClip);
        }

        private void renderSymbols(Canvas canvas) {
            var g = canvas.getGraphics();
            int yOffset = yOffset(canvas);

            int symbolSizeInt = canvas.scale(SYMBOL_SIZE);
            int yInterval = SYMBOL_SIZE + 7;

            int x = canvas.halfWidth() + canvas.scale(33);
            for (int cnt = 0; cnt < LENGTH; cnt ++) {
                int id = symbolIdAt(cnt);
                int y = canvas.halfHeight() - canvas.scale(100) + cnt * canvas.scale(yInterval) + yOffset;
                y = rotProgressDeltaY(canvas, yInterval, y);

                canvas.renderDebugString("" + id, Color.GREEN, x, y);
                g.drawImage(SYMBOLS[id], x, y, symbolSizeInt, symbolSizeInt, null);
            }
        }

        private int symbolIdAt(int cnt) {
            return symbolIndex(cnt - rot - 4);
        }

        private void renderNumbers(Canvas canvas) {
            var g = canvas.getGraphics();
            int yOffset = yOffset(canvas);
            int top = canvas.halfHeight() - canvas.scale(97) + yOffset;

            int numberSize = canvas.scale(NUMBER_SIZE);
            int yInterval = SYMBOL_SIZE + 7;

            int x = canvas.halfWidth() + canvas.scale(22);
            for (int cnt = 0; cnt < LENGTH; cnt ++) {
                int id = symbolIdAt(cnt);
                int num = numberIndex(id % DAYS_PER_MONTH);

                int y = top + cnt * canvas.scale(yInterval);
                y = rotProgressDeltaY(canvas, yInterval, y);

                g.drawImage(NUMBER_SYMBOLS[num], x, y, numberSize, numberSize, null);
            }
        }

        private int rotProgressDeltaY(Canvas canvas, int yInterval, int y) {
            int progressDeltaY = canvas.scale(rotProgressPercent() * yInterval);

            if (rotForwards) y -= progressDeltaY;
            else y += progressDeltaY;
            return y;
        }

        private int symbolIndex(int i) {
            return floorMod(i, LENGTH);
        }
        private int numberIndex(int i) {
            return floorMod(i, NUMBER_SYMBOLS.length);
        }
    }
    class MediumCog extends Cog {
        public MediumCog() {
            super("medium_cog", 20);
        }
        @Override
        public void render(Canvas canvas) {
            super.render(canvas);
        }
    }
    class SmallCog extends Cog {
        public SmallCog() {
            super("small_cog", 13);
        }
        @Override
        public void render(Canvas canvas) {
            super.render(canvas);
        }
    }
    abstract class Cog implements Tickable, Renderable {
        String name;
        int maxRot;
        int rot;
        boolean rotForwards = true;
        int rotProgressCount = 0;
        int rotProgressCountStep = 1;
        int rotProgress;
        int maxRotProgress = 3;
        int maxRotProgressCount = rotProgressCountStep * maxRotProgress * 1;

        public Cog(String name, int maxRot) {
            this.name = name;
            this.maxRot = maxRot;
        }
        @Override
        public void render(Canvas canvas) {
            int yOffset = yOffset(canvas);
            canvas.renderImage(Assets.texture("gui/mayan_calendar/" + name + "/" + rotProgress), canvas.halfWidth(), canvas.halfHeight() + yOffset, -0.5, -0.5);
        }

        protected int yOffset(Canvas canvas) {
            return focused == this ? canvas.scale(-15) : 0;
        }

        @Override
        public void tick() {
            if (rotProgressCount > 0) {
                rotProgressCount --;
                if (rotProgressCount % rotProgressCountStep == 0) {
                    if (rotForwards) rotProgress = (rotProgress + 1) % maxRotProgress;
                    else rotProgress = (rotProgress - 1 + maxRotProgress) % maxRotProgress;
                }
            }
        }

        public void increment() {
            rotProgressCount = maxRotProgressCount;
            rot ++;
            rot %= maxRot;
            rotForwards = true;
        }
        public void decrement() {
            rotProgressCount = maxRotProgressCount;
            rot --;
            if (rot < 0) rot += maxRot;
            rotForwards = false;
        }

        public boolean isRotating() {
            return rotProgressCount != 0;
        }
        protected double rotProgressPercent() {
            return (double) rotProgressCount / maxRotProgressCount;
        }
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);

        for (Clickable c : clickables) {
            if (c.onClick(click)) return;
        }

        // if no clickables successfully clicked, exit out of the focused cog
        focused = null;
    }

    @Override
    public void tick() {
        clickables.forEach(Clickable::tick);
        big.tick();
        med.tick();
        small.tick();
    }
}
