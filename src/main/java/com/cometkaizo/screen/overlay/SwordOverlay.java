package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.*;
import com.cometkaizo.screen.Canvas;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Screen overlay for the sword on the ludovisi ares sculpture
 */
public class SwordOverlay extends Overlay {
    private final Segment[] segments = {
            new Segment(0, false),
            new Segment(1, true),
            new Segment(2, false),
            new Segment(3, true),
            new Segment(4, true),
            new Segment(5, true),
            new Segment(6, false),
    };
    private final Text[] texts = {
            new Text("E", Assets.font(36), Color.RED, w -> w / 2 - 4, h -> h / 2 - 18, 100, false, false),
            new Text("V", Assets.font(36), Color.RED, w -> w / 2 - 4, h -> h / 2 - 10, 100, false, false),
            new Text("I", Assets.font(36), Color.RED, w -> w / 2 - 4, h -> h / 2 - 2, 100, false, false),
            new Text("L", Assets.font(36), Color.RED, w -> w / 2 - 4, h -> h / 2 + 10, 100, false, false)
    };
    private final Clickable submit = new ImageClickable(app, this::open, w -> w/2 - 90, h -> h/2 - 6, _ -> 26, _ -> 26, () -> "gui/sculpture/ares/submit", -2, -2);
    private final Runnable openAction;

    public SwordOverlay(GameApp app, Runnable openAction, Overlay next) {
        super(app, next);
        this.openAction = openAction;
    }

    private void open() {
        if (isCorrectCombo()) {
            openAction.run();
            app.narrate("You hear the squeaking of hinges. The chest of Ludovisi Ares swings open to reveal stone organs within.", next);
        } else {
            app.narrate("Nothing happens.", this);
            Assets.sound("wrong").play();
        }
    }

    private boolean isCorrectCombo() {
        for (var segment : segments) {
            if (segment.rotated) return false;
        }
        return true;
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture(getTexturePath()));

        Segment hovered = null;
        for (var segment : segments) {
            segment.render(canvas);
            if (segment.isHovered()) hovered = segment;
        }
        // render hovered on top
        if (hovered != null) hovered.render(canvas);

        for (var text : texts) text.render(canvas);
        submit.render(canvas);
    }

    private String getTexturePath() {
        return "gui/sculpture/ares/sword";
    }

    @Override
    public void tick() {
        super.tick();
        for (var segment : segments) segment.tick();
        submit.tick();
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        for (var segment : segments) segment.onClick(click);
        submit.onClick(click);
    }

    public class Segment extends ImageClickable {
        private int rotStep = -1, rotTick = 0;
        public final int id;
        public boolean rotated;
        public Segment(int id, boolean rotated) {
            super(SwordOverlay.this.app, () -> {}, w -> w/2 + 5 + id * 4, h -> h/2 - 18, _ -> 4, _ -> 45, null, -2, -2);
            this.id = id;
            this.rotated = rotated;
            this.action = this::rotate;
            this.texturePath = this::getTexturePath;
        }

        private boolean rotate() {
            if (rotStep == -1) {
                this.rotated = !this.rotated;
                rotStep = 0;
            }
            return true;
        }

        private String getTexturePath() {
            if (rotStep != -1) return "gui/sculpture/ares/blade_spin/" + rotStep;
            return "gui/sculpture/ares/blade" + (this.rotated ? "_back/" : "_front/") + id;
        }

        @Override
        public void tick() {
            super.tick();
            if (rotStep != -1) {
                if (--rotTick < 0) {
                    rotTick = 2;
                    if (++rotStep >= 3) {
                        rotStep = -1;
                    }
                }
            }
        }
    }
}
