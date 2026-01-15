package com.cometkaizo.world.entity;

import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable morse code poster
 */
public class MorseCodePoster extends Interactable {

    public MorseCodePoster(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    protected void interact() {
//        app.setOverlay(new LetterOverlay(app, MESSAGE, "letter"));
    }

    @Override
    protected String getTexturePath() {
        return "morse_code_poster";
    }
}
