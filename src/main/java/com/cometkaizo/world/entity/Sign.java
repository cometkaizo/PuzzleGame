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
            new Content("Gallery B", "Here is a wall of time-honored classics in sequence, but one is missing..."),
            new Content("Gallery C", "???"),
            new Content("Gallery D", "China called itself the \"Central Kingdom\""), // Russia, Kanagawa, Penguins
            new Content("Gallery E", "Colorful squares have hidden values. With exacting mathematical method, Piet Mondrian produces paintings which are precisely the sum of their parts."),
            new Content("Chess Board 1", "The black pawn arrives behind its brother.\n\nThe white pawn of the king steps forward.\n\nThe black knight jumps behind the pawn.\n\nWhat shape is formed?"),
            new Content("Chess Board 2", "The white rook attacks the black rook.\n\nThe black rook attacks the royal couple and no longer sees the white rook.\n\nThe white knight jumps in front of the queen to block the black rook's attack.\n\nWhere can the queen go?"),
            new Content("Chess Board 3", "The truth is hidden in what is not there."),
            new Content("Chess Board 4", "???"),
            new Content("Chess Board", "Maps are great. When you're lost, they let you CHECK WHERE you are."),
            new Content("The Thinker", "Thoughts become words and words become action."),
            new Content("Ludovisi Ares", "Ares, the Greek god of war.\n\nHis heart hangs heavy with EVIL. Offer him the food you ATE and your battles shall be WON. Trade mercy FOR violence, and your heart shall hang heavy TOO."),
            new Content("Mephistopheles and Margaretta", "???"),
            new Content("Venus de Milo", "Her beauty comes from what she is MISSING"),
            new Content("The Hope Diamond", "This famous diamond has a bloody history. It sparkles nicely in a strong light."),
            new Content("Flying Mercury", """
                    mercury, the swIft messenger, is captured mId-leap.
                    
                    his arms eVoke motion, extended as if in flIght.
                    
                    the musculature of his chest shows an Incredible attention to anatomical detail.
                    
                    in his right hand, he holds a diVine object.
                    
                    he shImmers, catching lIght on sculpted rIdges.
                    
                    the bronze surface glows as shadows drIve across the pedestal, emphasizing moVement and energy.
                    """),
            new Content("Rosetta Stone", "This 2,000-year-old artifact provides the key for linguists to unlock the meaning of the ÇäÑÑŽ language, revealing vast knowledge of the calendar systems of ancient civilizations."),
            new Content("Archimedes Death Ray", "This legendary weapon was supposedly used to focus sunlight to set attacking Roman ships ablaze during the Siege of Syracuse."),
    };

    private String title, desc;
    private String overlayVariant;

    public Sign(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    @Override
    public void reset() {
        super.reset();
        int messageVariant = originalArgs.nextInt(1) - 1;
        title = CONTENT[messageVariant].title;
        desc = CONTENT[messageVariant].desc;

        overlayVariant = originalArgs.next("regular");
    }

    @Override
    protected void interact() {
        app.setOverlay(new SignOverlay(app, title, desc, overlayVariant));
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
