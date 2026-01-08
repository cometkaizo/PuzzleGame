package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.item.NoteItem;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Screen overlay for the "see" screen of a note holder
 */
public class NoteHolderSeeOverlay extends Overlay {
    private final NoteItem[] notes;

    public NoteHolderSeeOverlay(GameApp app, NoteItem[] notes, Overlay next) {
        super(app, next);
        this.notes = notes;
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        for (var note : notes) if (note != null) note.render(canvas);

        canvas.renderCenteredImage(Assets.texture("gui/note_holder/glass"));
    }
}
