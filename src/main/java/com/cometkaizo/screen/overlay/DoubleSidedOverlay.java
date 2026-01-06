package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Screen overlay for the double-sided statue
 */
public class DoubleSidedOverlay extends Overlay {
    private boolean mirrorRemoved;

    public DoubleSidedOverlay(GameApp app, boolean mirrorRemoved) {
        super(app);
        this.mirrorRemoved = mirrorRemoved;
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderCenteredImage(Assets.texture(getTexturePath()));
    }

    private String getTexturePath() {
        return "gui/sculpture/double_sided/" + (mirrorRemoved ? "no_mirror" : "regular");
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
