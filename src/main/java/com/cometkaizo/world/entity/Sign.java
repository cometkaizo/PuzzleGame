package com.cometkaizo.world.entity;

import com.cometkaizo.screen.overlay.SignOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Interactable sign
 */
public class Sign extends Interactable {

    public static final Content[] CONTENT = {
            new Content("Mayan Calendar", "The ancient Mayans used a system of interlocking cycles to track time. Each day, all cycles are incremented by one."),
            new Content("Gallery A", "???"),
            new Content("Gallery B", "Wall of time-honored classics"),
            new Content("Gallery C", "???"),
            new Content("Gallery D", "China called itself the \"Central Kingdom\""), // Russia, Kanagawa, Penguins
            new Content("Gallery E", "Colorful squares have hidden values. With exacting mathematical method, Piet Mondrian produces paintings which are precisely the sum of their parts."),
            new Content("Chess Board 1", "The black pawn arrives behind its brother.\n\nThe white pawn of the king steps forward.\n\nThe black knight jumps behind the pawn.\n\nWhat shape is formed?"),
            new Content("Chess Board 2", "The white rook attacks the black rook.\n\nThe black rook attacks the royal couple and no longer sees the white rook.\n\nThe white knight jumps directly in front of the queen.\n\nWhat can the queen see?"),
            new Content("Chess Board 3", "The truth is hidden in what is not there."),
            new Content("Chess Board 4", "???"),
            new Content("Chess Board", "Maps are great. When you're lost, they let you CHECK WHERE you are."),
            new Content("The Thinker", "Thoughts become words and words become action."),
            new Content("Cupid and Psyche", "???"),
            new Content("Mephistopheles and Margaretta", "???"),
            new Content("Venus de Milo", "???"),
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
