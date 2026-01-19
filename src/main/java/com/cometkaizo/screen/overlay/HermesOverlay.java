package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.game.item.FeatherItem;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.ImageClickable;
import com.cometkaizo.screen.Text;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-14
 * Description: Screen overlay for the ludovisi ares sculpture
 */
public class HermesOverlay extends Overlay {
    private final Wing[] wings = {
            new Wing(1, 10, 54),
            new Wing(2, 36, -2),
            new Wing(3, 33, -45),
            new Wing(4, 19, -46),
            new Wing(6, -7, -60),
            new Wing(5, 0, -60),
    };
    private final Integer[] correctWingCombo = {2, 6, 1, 5, 3, 4};
    private final Queue<Integer> lastClickedWings = new ArrayDeque<>(List.of(0, 0, 0, 0, 0, 0));
    private boolean open;
    private final Runnable openAction;

    public HermesOverlay(GameApp app, boolean open, Runnable openAction) {
        super(app);
        this.open = open;
        this.openAction = openAction;
    }

    private void click(int id) {
        if (open) return;
        Assets.sound("hermes/" + id).play();
        lastClickedWings.poll();
        lastClickedWings.add(id);
        if (isCorrectCombo()) open();
    }
    private boolean isCorrectCombo() {
        return Arrays.equals(correctWingCombo, lastClickedWings.toArray(Integer[]::new));
    }

    private void open() {
        open = true;
        Assets.sound("hermes/solve").play();
        openAction.run();
        app.narrate("Hermes's hand unclasps and the feather of truth falls out. You take it.", this);
        app.getGame().getInventory().add(new FeatherItem());
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture(getTexturePath()));

        for (var wing : wings) wing.render(canvas);
    }

    private String getTexturePath() {
        return "gui/sculpture/hermes/" + (open ? "solved" : "regular");
    }

    @Override
    public void tick() {
        super.tick();
        for (var wing : wings) wing.tick();
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        for (var wing : wings) wing.onClick(click);
    }

    public class Wing extends ImageClickable {
        public final int id;
        public final Text text;

        public Wing(int id, int dx, int dy) {
            super(HermesOverlay.this.app, () -> {}, w -> w/2 + dx, h -> h/2 + dy, _ -> id == 6 ? 7 : 14, _ -> 10, () -> "gui/sculpture/hermes/wing/" + id, -2, -2);
            this.id = id;
            action = this::click;
            text = new Text("" + id, Assets.font(24), Color.RED, w -> w/2 + dx + 3, h -> h/2 + dy - 7, 100, false, false);
        }

        private boolean click() {
            HermesOverlay.this.click(id);
            return true;
        }

        @Override
        public void render(Canvas canvas) {
            super.render(canvas);
            text.render(canvas);
        }
    }
}
