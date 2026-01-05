package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.LetterOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable letter
 */
public class Letter extends Interactable {

    public static final String MESSAGE = """
            Hello son,
            
            Here is the museum at last - take a deep breath and feel the history in the air.
            
            As we have agreed, you may take care of the museum until I return. You know that this museum is everything to me.
            
            I am writing from Egypt. The fools have refused my generous offer for the final artifact in this collection, but they will not escape me. I shall be finding them in person to discuss the matter further. Three drums shall mark the end of the era.
            
            Sincerely,
            Your father
            """;

    public Letter(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    protected void interact() {
        app.setOverlay(new LetterOverlay(app, MESSAGE, "letter"));
    }

    @Override
    protected String getTexturePath() {
        return "letter";
    }
}
