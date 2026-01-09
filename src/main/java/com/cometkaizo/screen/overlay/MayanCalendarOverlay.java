package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.*;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.world.Tickable;

import java.awt.*;
import java.util.stream.IntStream;

import static java.lang.Math.floorMod;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Screen overlay for the mayan calendar
 */
public class MayanCalendarOverlay extends Overlay {
    public static final Image[] NUMBER_SYMBOLS = IntStream.range(0, 21).mapToObj(i -> Assets.texture("gui/mayan_calendar/number/" + i)).toArray(Image[]::new);
    public static final Image[] TZOLKIN_SYMBOLS = IntStream.range(0, 20).mapToObj(i -> Assets.texture("gui/mayan_calendar/tzolkin_symbol/" + i)).toArray(Image[]::new);
    public static final Image[] HAAB_SYMBOLS = IntStream.range(0, 19).mapToObj(i -> Assets.texture("gui/mayan_calendar/haab_symbol/" + i)).toArray(Image[]::new);
    public static final int NUMBER_SIZE = 9;
    public static final int SYMBOL_SIZE = 16;

    private final Cog big = new BigCog(), med = new MediumCog(), small = new SmallCog();
    private Cog focused = null;
    private final Clickable upButton = new ImageClickable(app, this::increment, w -> w / 2 + 62, h -> h / 2 - 50, _ -> 23, _ -> 23, () -> "gui/mayan_calendar/up_button", -2, -2),
            openButton = new ImageClickable(app, this::open, w -> w / 2 + 62, h -> h / 2 - 11, _ -> 23, _ -> 23, () -> "gui/mayan_calendar/open_button", -88, -2) {
                @Override protected boolean isOutlined() {
                    return super.isOutlined() && focused == null;
                }},
            downButton = new ImageClickable(app, this::decrement, w -> w / 2 + 62, h -> h / 2 + 26, _ -> 23, _ -> 23, () -> "gui/mayan_calendar/down_button", -2, -2);

    private final int[] correctPaintingsCombo, correctSculpturesCombo, correctModernCombo, correctArtifactsCombo;

    public MayanCalendarOverlay(GameApp app, int[] correctPaintingsCombo, int[] correctSculpturesCombo, int[] correctModernCombo, int[] correctArtifactsCombo) {
        super(app);
        this.correctPaintingsCombo = correctPaintingsCombo;
        this.correctSculpturesCombo = correctSculpturesCombo;
        this.correctModernCombo = correctModernCombo;
        this.correctArtifactsCombo = correctArtifactsCombo;
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

        if (isCurrentCombo(correctPaintingsCombo)) {
            app.getGame().paintingsDoor.open();
            app.setOverlay(new NarrationOverlay(app, "The door to the paintings room swings open."));
        } else if (isCurrentCombo(correctSculpturesCombo)) {
            app.getGame().sculpturesDoor.open();
            app.setOverlay(new NarrationOverlay(app, "The door to the sculptures room swings open."));
        } else if (isCurrentCombo(correctModernCombo)) {
            app.getGame().modernDoor.open();
            app.setOverlay(new NarrationOverlay(app, "The door to the modern history room swings open."));
        } else if (isCurrentCombo(correctArtifactsCombo)) {
            app.getGame().artifactsDoor.open();
            app.setOverlay(new NarrationOverlay(app, "The door to the artifacts room swings open."));
        } else {
            app.setOverlay(new NarrationOverlay(app, "Nothing happens.", this));
            Assets.sound("wrong").play();
        }

        return true;
    }

    private boolean isCurrentCombo(int[] combo) {
        return combo[0] == small.rot && combo[1] == med.rot && combo[2] == big.rot;
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
        openButton.render(canvas);

        if (isFocusing()) {
            canvas.fillScreen(new Color(0, 0, 0, 200));
            focused.render(canvas);
        }

        upButton.render(canvas);
        downButton.render(canvas);
    }

    class BigCog extends Cog {
        static final int LENGTH = 365;
        static final int DAYS_PER_MONTH = 20;
        static final Image[] SYMBOLS = IntStream.range(0, LENGTH).mapToObj(i -> HAAB_SYMBOLS[i / 20]).toArray(Image[]::new);

        public BigCog() {
            super("big_cog", LENGTH, 20, -75, 30, 151, -4, 0);
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
                int y = canvas.halfHeight() + canvas.scale(84) - cnt * canvas.scale(yInterval) + yOffset;
                y += rotProgressDeltaY(canvas, yInterval);

                canvas.renderDebugString("" + id, Color.GREEN, x, y);
                g.drawImage(SYMBOLS[id], x, y, symbolSizeInt, symbolSizeInt, null);
            }
        }

        private int symbolIdAt(int cnt) {
            return symbolIndex(cnt + rot - 4);
        }

        private void renderNumbers(Canvas canvas) {
            var g = canvas.getGraphics();
            int yOffset = yOffset(canvas);
            int bottom = canvas.halfHeight() + canvas.scale(88) + yOffset;

            int numberSize = canvas.scale(NUMBER_SIZE);
            int yInterval = SYMBOL_SIZE + 7;

            int x = canvas.halfWidth() + canvas.scale(23);
            for (int cnt = 0; cnt < LENGTH; cnt ++) {
                int id = symbolIdAt(cnt);
                int num = numberIndex(id % DAYS_PER_MONTH);

                int y = bottom - cnt * canvas.scale(yInterval);
                y += rotProgressDeltaY(canvas, yInterval);

                g.drawImage(NUMBER_SYMBOLS[num], x, y, numberSize, numberSize, null);
            }
        }

        private int rotProgressDeltaY(Canvas canvas, int yInterval) {
            int progressDeltaY = canvas.scale(rotProgressPercent() * yInterval);

            if (rotForwards) return -progressDeltaY;
            else return progressDeltaY;
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
            super("medium_cog", TZOLKIN_SYMBOLS.length, -119, -67, 135, 135, -6, -6);
        }
        @Override
        public void render(Canvas canvas) {
            super.render(canvas);
            var g = canvas.getGraphics();
            var oldTransform = g.getTransform();
            int yOffset = yOffset(canvas);
            double twoPi = 2 * Math.PI;
            double angleOffset = twoPi * rotPercent();
            int symbolSize = canvas.scale(SYMBOL_SIZE);

            int centerX = canvas.scale(canvas.halfPixelWidth() - 51.5);
            int centerY = canvas.scale(canvas.halfPixelHeight() + 0.5) + yOffset;
            canvas.renderDebugRect(centerX, centerY, 1, 1, Color.GREEN);

            for (int cnt = 0; cnt < maxRot; cnt ++) {
                double percent = (double) cnt / maxRot;
                double angle = -twoPi * percent + angleOffset;

                int x = centerX + (int) (Math.cos(angle) * canvas.scale(53));
                int y = centerY + (int) (Math.sin(angle) * canvas.scale(53));

                g.rotate(angle, x, y);

                g.drawImage(TZOLKIN_SYMBOLS[cnt], x, y - symbolSize / 2, symbolSize, symbolSize, null);
                canvas.renderDebugString("" + cnt, Color.GREEN, x, y);

                g.setTransform(oldTransform);
            }
        }

        @Override
        protected boolean isOutlined() {
            return super.isOutlined() && (focused == this || !small.isHovered());
        }
    }
    class SmallCog extends Cog {
        public SmallCog() {
            super("small_cog", 13, -58, -27, 55, 55, -5, -5);
        }
        @Override
        public void render(Canvas canvas) {
            super.render(canvas);
            var g = canvas.getGraphics();
            var oldTransform = g.getTransform();
            int yOffset = yOffset(canvas);
            double twoPi = 2 * Math.PI;
            double angleOffset = twoPi * rotPercent();
            int numberSize = canvas.scale(NUMBER_SIZE);

            int centerX = canvas.scale(canvas.halfPixelWidth() - 30.5);
            int centerY = canvas.scale(canvas.halfPixelHeight() + 0.5) + yOffset;
            canvas.renderDebugRect(centerX, centerY, 1, 1, Color.GREEN);

            for (int cnt = 0; cnt < maxRot; cnt ++) {
                double percent = (double) cnt / maxRot;
                double angle = -twoPi * percent + angleOffset;

                int x = centerX + (int) (Math.cos(angle) * canvas.scale(17.5));
                int y = centerY + (int) (Math.sin(angle) * canvas.scale(17.5));

                g.rotate(angle, x, y);

                g.drawImage(NUMBER_SYMBOLS[cnt], x, y - numberSize / 2, numberSize, numberSize, null);
                canvas.renderDebugString("" + cnt, Color.GREEN, x, y);

                g.setTransform(oldTransform);
            }
        }
    }
    abstract class Cog extends ImageClickable implements Tickable, Renderable {
        String name;
        int maxRot;
        int rot;
        boolean rotForwards = true;
        int rotProgressCount = 0;
        int rotProgressCountStep = 1;
        int rotProgress;
        int maxRotProgress = 3;
        int maxRotProgressCount = rotProgressCountStep * maxRotProgress * 1;

        public Cog(String name, int maxRot, int dx, int dy, int w, int h, int texDX, int texDY) {
            super(MayanCalendarOverlay.this.app, () -> {}, w1 -> w1/2 + dx, null, w1 -> w, h1 -> h, null, texDX -2, texDY -2);
            this.action = () -> focus(this);
            this.y = w1 -> w1/2 + dy + (focused == this ? -15 : 0);
            this.texturePath = () -> "gui/mayan_calendar/" + name + "/" + rotProgress;
            this.name = name;
            this.maxRot = maxRot;
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
        protected double rotPercent() {
            double main = (double) rot / maxRot;
            double step = (1D / maxRot) * (rotForwards ? -rotProgressPercent() : rotProgressPercent());
            return main + step;
        }
        protected double rotProgressPercent() {
            return (double) rotProgressCount / maxRotProgressCount;
        }

        @Override
        protected boolean isOutlined() {
            return super.isOutlined() && (focused == null || focused == this);
        }
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);

        if (upButton.onClick(click)) return;
        if (openButton.onClick(click)) return;
        if (downButton.onClick(click)) return;
        if (small.onClick(click)) return;
        if (med.onClick(click)) return;
        if (big.onClick(click)) return;

        // if no clickables successfully clicked, exit out of the focused cog
        focused = null;
    }

    @Override
    public void tick() {
        upButton.tick();
        openButton.tick();
        downButton.tick();
        big.tick();
        med.tick();
        small.tick();
    }
}
