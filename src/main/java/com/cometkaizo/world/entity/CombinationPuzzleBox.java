package com.cometkaizo.world.entity;

import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.overlay.CombinationLockOverlay;
import com.cometkaizo.screen.overlay.LetterOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;

public class CombinationPuzzleBox extends Interactable {
    private static final String[] MESSAGES = {
            """
            message A
            """,
            """
            message B
            """,
            """
            message C
            """,
            """
            message D
            """,
            """
            message E
            """
    };


    private String correctCombination;
    private String[] digitOptions;
    private int w, h;
    private int variant;
    private String message;

    private boolean open;
    private long openMessageTick = -1;


    public CombinationPuzzleBox(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    protected void interact() {
        if (!open) app.setOverlay(new CombinationLockOverlay(app, correctCombination, digitOptions, this::open));
        else openMessage();
    }
    private void openMessage() {
        app.setOverlay(new LetterOverlay(app, message, "note"));
    }

    private void open() {
        open = true;
        openMessageTick = game.tick + 20;
    }

    @Override
    public void reset() {
        super.reset();
        open = false;
        w = originalArgs.nextInt(1);
        h = originalArgs.nextInt(1);
        correctCombination = originalArgs.next("");
        digitOptions = originalArgs.next("").split(" ");

        variant = originalArgs.nextInt(1);
        message = MESSAGES[variant - 1];

        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable((double) w, h));
    }

    @Override
    public void tick() {
        super.tick();
        if (game.tick == openMessageTick) openMessage();
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
        return "combination_puzzle_box/" + variant;
    }
}
