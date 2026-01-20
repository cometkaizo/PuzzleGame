package com.cometkaizo.world.entity;

import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.overlay.NoteOverlay;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-16
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

    /// Creates a new letter entity
    public Letter(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
    }

    /// Opens the interaction overlay when the player interacts with this entity
    @Override
    protected void interact() {
        app.setOverlay(new NoteOverlay(app, MESSAGE, "letter"));
    }

    /// Renders this entity to the screen
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderDebugBoundingBox(boundingBox, Color.BLUE);
    }

    /// Gets the path to the texture
    @Override
    protected String getTexturePath() {
        return "letter";
    }

    /// Gets the y translation applied to the texture, as a percentage of the height of the image
    @Override
    protected double getTextureDeltaYFactor() {
        return -2.5;
    }

    /// The y-value at which this entity is compared to other entities to determine which is rendered in front
    @Override
    public double getRenderY() {
        return position.y + 0.8;
    }
}
