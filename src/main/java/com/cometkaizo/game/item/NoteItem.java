package com.cometkaizo.game.item;

import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Renderable;

public class NoteItem extends Item implements Renderable {
    @Override
    protected String getTexturePathImpl() {
        return "note";
    }

    @Override
    public String getName() {
        return "Slot";
    }

    @Override
    public void render(Canvas canvas) {

    }
}
