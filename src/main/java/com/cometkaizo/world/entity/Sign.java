package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.SignOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

public class Sign extends Interactable {

    public static final Content[] CONTENT = {
            new Content("Mayan Calendar", "The ancient Mayans used a system of interlocking cycles to track time. Each day, all cycles are incremented by one."),
            new Content("Gallery A"),
            new Content("Gallery B", "The artworks on this wall contemplate the everyday cycle of rebirth"),
            new Content("Gallery C"),
            new Content("Gallery D", "China called itself the \"Central Kingdom\""),
            new Content("Gallery E", "Colorful squares have hidden values. These paintings are the sum of their parts."),
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

    @Override
    protected int getTextureDeltaX() {
        return -2;
    }

    @Override
    protected int getTextureDeltaY() {
        return 2;
    }
}
