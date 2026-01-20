package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.item.NoteItem;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-08
 * Description: Screen overlay for the "see" screen of a note holder
 */
public class NoteHolderSeeOverlay extends Overlay {
    private final NoteItem[] notes;

    /// Creates a new overlay
    public NoteHolderSeeOverlay(GameApp app, NoteItem[] notes, Overlay next) {
        super(app, next);
        this.notes = notes;
    }

    /// Renders this overlay to the screen
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture("gui/note_holder/light"));
        for (var note : notes) if (note != null) note.render(canvas);

        canvas.renderCenteredImage(Assets.texture("gui/note_holder/glass"));
    }
}
