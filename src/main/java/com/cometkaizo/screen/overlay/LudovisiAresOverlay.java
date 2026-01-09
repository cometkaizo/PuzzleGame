package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Text;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Screen overlay for the ludovisi ares sculpture
 */
public class LudovisiAresOverlay extends Overlay {
    private final Text text = new Text("E V I L\n2 4 1 8", Assets.font(24), Color.RED,
            w -> w/2 + 14, h -> h/2 - 48, 100, false, false);

    public LudovisiAresOverlay(GameApp app) {
        super(app);
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture(getTexturePath()));

        text.render(canvas);
    }

    private String getTexturePath() {
        return "gui/sculpture/ares/regular";
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
    }
}
