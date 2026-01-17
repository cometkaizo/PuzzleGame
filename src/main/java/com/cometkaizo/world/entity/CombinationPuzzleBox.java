package com.cometkaizo.world.entity;

import com.cometkaizo.game.GameState;
import com.cometkaizo.game.item.ChessKeyItem;
import com.cometkaizo.game.item.MachinePieceItem;
import com.cometkaizo.game.item.NoteItem;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.overlay.CombinationLockOverlay;
import com.cometkaizo.screen.overlay.NoteOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;
import java.util.Arrays;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable combination puzzle box
 */
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


    private String[] correctCombination;
    private String[][] digitOptions;
    private int w, h;
    private int variant;
    private String overlayVariant;
    private boolean solid;

    private boolean open;
    private long openTick = -1;
    private CombinationLockOverlay overlay;


    public CombinationPuzzleBox(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    protected void interact() {
        if (!open) app.setOverlay(overlay);
        else if (opensMessage()) openMessage();
        else app.narrate("You've already solved this lock.", null);
    }
    private void openMessage() {
        app.setOverlay(new NoteOverlay(app, MESSAGES[variant - 1], "mayan_note"));
    }

    @Override
    protected void solve() {
        actuallyOpen();
    }

    private void open() {
        open = true;
        openTick = game.tick + 20;
    }

    private void actuallyOpen() {
        open = true;
        if (opensMessage()) openMessage();
        else if (givesItem()) giveItem();
    }
    private boolean opensMessage() {
        return variant <= 5;
    }
    private boolean givesItem() {
        return variant >= 6 && variant <= 12;
    }
    private void giveItem() {
        if (variant == 6) {
            game.getInventory().add(new MachinePieceItem());
            app.narrate("Inside the locked box is a part from a machine. You take it.", null);
        } else if (variant == 7) {
            game.getInventory().add(new ChessKeyItem());
            app.narrate("Inside the locked box is a key. You take it.", null);
        } else if (8 <= variant && variant <= 11) {
            game.getInventory().add(new NoteItem(variant - 8));
            app.narrate("Inside the locked box is a note. You take it.", null);
        } else if (variant == 12) {
            game.getInventory().add(new MachinePieceItem());
            app.narrate("Inside the locked box is a part from a machine. You take it.", null);
        }
    }

    @Override
    public void reset() {
        super.reset();
        w = originalArgs.nextInt(1);
        h = originalArgs.nextInt(1);

        String comboRaw = originalArgs.next("");
        correctCombination = comboRaw.contains("|") ? comboRaw.split("\\|") : comboRaw.split("");

        var digitsRaw = Arrays.stream(originalArgs.next("").split(" "));
        digitOptions = (comboRaw.contains("|") ?
                digitsRaw.map(s -> s.split("\\|")) :
                digitsRaw.map(s -> s.split("")))
                .toArray(String[][]::new);

        variant = originalArgs.nextInt(1);
        overlayVariant = originalArgs.next("regular");

        solid = originalArgs.next("solid").equalsIgnoreCase("solid");

        boundingBox = new BoundingBox(Vector.mutable(0D, 0D), Vector.immutable((double) w, h));

        overlay = new CombinationLockOverlay(app, correctCombination, digitOptions, this::open, overlayVariant);

        open = originalGameState.locksOpen.getOrDefault(variant, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (game.tick == openTick) actuallyOpen();
    }

    @Override
    public boolean isSolid(Entity entity) {
        return solid;
    }

    @Override
    public void write(GameState state) {
        super.write(state);
        state.locksOpen.put(variant, open);
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
