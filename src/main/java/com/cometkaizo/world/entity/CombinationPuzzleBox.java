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
            
            The expedition to the north has just returned. The sun has not shaken off the night, but voices and chants can be heard from within the ruler's temple. Expeditions like to the north used to be rare, but recent developments mean that can no longer be avoided. Currently, the captain of the exhibition presents himself before the ruler and his advisor. The discovery would endanger his entire people, but he has seen a path of escape.
            
            "-- and my Lord, the Darkness has been seen, but so has the salvation. The calendar - it is wrong. The date must be shifted --"
            
            The captain's voice cuts off, and he bends forward, choking.
            """,
            """
            Note #2
            
            The captain is held up by two members of the expedition. His face is contorted with concentration. For the past sun cycle he has remained here before the ruler, whose advisor asks a seemingly endless number of questions. The ruler and the advisor are fixated on information about the Darkness. But the captain, contrary to his usual competence, cannot seem to answer. Since the previous dawn, every time he goes to speak about the Darkness, his throat contracts and he chokes on his own knowledge. It seems that his communication of the Darkness cannot be intentional.
            
            Finally, the ruler runs out of patience. He dismisses the captain and goes to leave, but the captain suddenly stops trying to speak and raises his fist in the air. The ruler turns. With an exaggerated motion, the captain brings his fist down and hits the ground at his feet. He raises it back up slowly, and repeats the gesture two more times. The advisor understands - at last communication has been reestablished between them. He orders the inner wheel to be incremented 3 times.
            """,
            """
            Note #3
            
            The ruler sits in intense thought. It seems the Darkness has powers far above their comprehension. The captain can now no longer move his arms, but he evidently has more to say. His eyes have been darting about in a panicked frenzy. The ruler had avoided looking at the captain's deranged appearance, but his advisor cannot seem to avert his gaze. Presently, the advisor startles up.
            
            "I understand!" he exclaims, looking at the captain's spinning eyes. The eyes spin exactly two full rotations, then stop. The captain had been repeating the motion for the whole night. The outer wheel is incremented 2 times.
            """,
            """
            Note #4
            
            A day passes. The large rightmost wheel is decremented 4 times.
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
