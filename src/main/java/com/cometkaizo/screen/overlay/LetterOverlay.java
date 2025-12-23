package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Text;

import java.awt.*;

public class LetterOverlay extends Overlay {
    private final Text content = new Text("""
            Hello son,
            
            Here is the museum at last - take a deep breath and feel the history in the air.
            
            As we have agreed, you may take care of the museum until I return. You know that this museum is everything to me.
            
            I am writing from Egypt. The nine rulers have refused my generous offer for the final artifact in this collection, but they will not escape me. I shall be finding them in person to discuss the matter further.
            
            Sincerely,
            Your father
            """, Assets.font("BoldPixels", 20), Color.BLACK,
            w -> w / 2 - 24,
            h -> h / 2 - 28,
            47);

    public LetterOverlay(GameApp app) {
        super(app);
    }

    @Override
    public void render(Canvas canvas) {
        canvas.fillScreen(new Color(0, 0, 0, 200));
        canvas.renderImage(Assets.texture("gui/letter"), canvas.halfWidth(), canvas.halfHeight(), -0.5, -0.5);

        content.render(canvas);
    }
}
