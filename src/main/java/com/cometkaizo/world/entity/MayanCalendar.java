package com.cometkaizo.world.entity;

import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.overlay.MayanCalendarOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;
import java.util.Arrays;
/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable mayan calendar
 */
public class MayanCalendar extends Interactable {
    private int[] correctPaintingsCombo, correctSculpturesCombo, correctArtifactsCombo;

    public MayanCalendar(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable(2D, 2D));
    }

    @Override
    protected void interact() {
        app.setOverlay(new MayanCalendarOverlay(app, correctPaintingsCombo, correctSculpturesCombo, correctArtifactsCombo));
    }

    @Override
    public void reset() {
        super.reset();
        correctPaintingsCombo = nextMayanCombo(originalArgs);
        correctSculpturesCombo = nextMayanCombo(originalArgs);
        correctArtifactsCombo = nextMayanCombo(originalArgs);
    }
    private int[] nextMayanCombo(Args args) {
        return Arrays.stream(args.next(" ").split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderDebugBoundingBox(boundingBox, Color.DARK_GRAY);
    }

    @Override
    protected String getTexturePath() {
        return "mayan_calendar";
    }
}
