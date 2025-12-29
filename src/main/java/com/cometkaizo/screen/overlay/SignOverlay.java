package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Text;

import java.awt.*;

public class SignOverlay extends Overlay {
    private final Text title, desc;
    private final String variant;

    public SignOverlay(GameApp app, String title, String desc, String variant) {
        super(app);
        this.title = new Text(title, Assets.font("BoldPixels", 40), Color.BLACK,
                w -> w / 2 - 87,
                h -> h / 2 - 60,
                174, false);
        this.desc = new Text(desc, Assets.font("BoldPixels", 20), Color.BLACK,
                w -> w / 2 - 87,
                h -> h / 2 - 45,
                174, false);
        this.variant = variant;
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderImage(Assets.texture("gui/" + variant), canvas.halfWidth(), canvas.halfHeight(), -0.5, -0.5);

        title.render(canvas);
        desc.render(canvas);
    }
}
