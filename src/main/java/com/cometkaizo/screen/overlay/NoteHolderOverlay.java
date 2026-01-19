package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.game.item.NoteItem;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Clickable;
import com.cometkaizo.screen.ImageClickable;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-08
 * Description: Screen overlay for a note holder
 */
public class NoteHolderOverlay extends Overlay {

    private final boolean lit;
    private final Clickable seeClickable;
    private final NoteItem[] notes;
    private final Slot[] slots;

    public NoteHolderOverlay(GameApp app, boolean lit, NoteItem[] notes) {
        super(app);
        this.lit = lit;
        this.notes = notes;

        this.slots = new Slot[notes.length];
        for (int i = 0; i < notes.length; i++) slots[i] = new Slot(i);

        seeClickable = new ImageClickable(this.app,
                () -> {
                    if (lit) app.setOverlay(new NoteHolderSeeOverlay(app, this.notes, this));
                    else {
                        app.narrate("There's no light shining through here", this);
                        Assets.sound("wrong").play();
                    }
                },
                w -> w/2 - 16, h -> h/2 - 50, _ -> 32, _-> 32,
                () -> "gui/note_holder/see", -2, -2);
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
//        canvas.renderCenteredImage(Assets.texture("gui/note_holder/regular"));

        for (Slot slot : slots) slot.render(canvas);
        seeClickable.render(canvas);
    }

    public class Slot extends ImageClickable {
        private final int index;

        public Slot(int index) {
            super(NoteHolderOverlay.this.app, () -> {}, w -> w/2 - 32, h -> h/2 + index * 10, _ -> 64, _ -> 10, null, -2, -2);
            this.index = index;
            this.action = () -> {
                if (note() == null) placeNote();
                else pickUpNote();
                return true;
            };
            this.texturePath = () -> "gui/note_holder/" + (note() == null ? "slot" : "slot_filled");
        }

        private NoteItem note() {
            return notes[index];
        }

        private void placeNote() {
            app.setOverlay(new InventoryOverlay(app, item -> {
                if (item instanceof NoteItem note) {
                    notes[index] = note;
                    app.getGame().getInventory().remove(note);
                    app.setOverlay(NoteHolderOverlay.this);
                } else {
                    app.narrate("This item cannot be placed here", NoteHolderOverlay.this);
                    Assets.sound("wrong").play();
                }
            }, NoteHolderOverlay.this));
        }

        private void pickUpNote() {
            app.getGame().getInventory().add(note());
            notes[index] = null;
        }

        @Override
        public void render(Canvas canvas) {
            super.render(canvas);
        }
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        for (Slot slot : slots) slot.onClick(click);
        seeClickable.onClick(click);
    }

    @Override
    public void tick() {
        super.tick();
        for (var slot : slots) slot.tick();
        seeClickable.tick();
    }
}
