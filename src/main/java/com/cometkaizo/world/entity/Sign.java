package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.SignOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

public class Sign extends Interactable {

    public static final Content[] CONTENT = {
            new Content("Mayan Calendar"),
            new Content("Gallery A"),
            new Content("Gallery B"),
            new Content("Gallery C", "China called itself the \"Central Kingdom\""),
            new Content("Gallery D"),
            new Content("Gallery E"),
    };

    private String title, desc;

    public Sign(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    public void reset() {
        super.reset();
        int messageVariant = originalArgs.nextInt(1) - 1;
        title = CONTENT[messageVariant].title;
        desc = CONTENT[messageVariant].desc;
    }

    @Override
    protected void interact() {
        app.setOverlay(new SignOverlay(app, title, desc, "sign"));
    }

    @Override
    protected String getTexturePath() {
        return "sign";
    }

    public record Content(String title, String desc) {
        public Content(String title) {
            this(title, "");
        }
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }
}
