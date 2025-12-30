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
            Note #1
            
            The day is 12 Chikchan 5 Uo.
            """,
            """
            Note #2
            
            The inner wheel is incremented 3 times.
            """,
            """
            Note #3
            
            The outer wheel is incremented 2 times.
            """,
            """
            Note #4
            
            The large rightmost wheel is decremented 4 times.
            """,
            """
            Note #5
            
            Finally, five days pass.
            """
    };


    private String correctCombination;
    private String[] digitOptions;
    private int w, h;
    private int variant;
    private String overlayVariant;
    private String message;
    private boolean solid;

    private boolean open;
    private long openMessageTick = -1;


    public CombinationPuzzleBox(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    protected void interact() {
        if (!open) app.setOverlay(new CombinationLockOverlay(app, correctCombination, digitOptions, this::open, overlayVariant));
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
        overlayVariant = originalArgs.next("regular");

        solid = originalArgs.next("solid").equalsIgnoreCase("solid");

        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable((double) w, h));
    }

    @Override
    public void tick() {
        super.tick();
        if (game.tick == openMessageTick) openMessage();
    }

    @Override
    public boolean isSolid(Entity entity) {
        return solid;
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderDebugBoundingBox(boundingBox, Color.DARK_GRAY);
    }

    @Override
    public double getRenderY() {
        return getY() + 0.8;
    }

    @Override
    protected String getTexturePath() {
        return "combination_puzzle_box/" + variant;
    }

    @Override
    protected int getTextureDeltaX() {
        return -2;
    }
    @Override
    protected int getTextureDeltaY() {
        return 2;
    }
}
