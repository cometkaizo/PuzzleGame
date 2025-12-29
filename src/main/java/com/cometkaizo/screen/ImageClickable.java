package com.cometkaizo.screen;

import com.cometkaizo.app.GameApp;

import java.awt.*;
import java.util.function.BooleanSupplier;
import java.util.function.IntUnaryOperator;

public class ImageClickable extends Clickable {
    protected final String texturePath;
    protected final int textureXOffset, textureYOffset;

    public ImageClickable(GameApp app, BooleanSupplier action, IntUnaryOperator x, IntUnaryOperator y, IntUnaryOperator w, IntUnaryOperator h, String texturePath, int textureXOffset, int textureYOffset) {
        super(app, action, x, y, w, h);
        this.texturePath = texturePath;
        this.textureXOffset = textureXOffset;
        this.textureYOffset = textureYOffset;
    }

    public ImageClickable(GameApp app, Runnable action, IntUnaryOperator x, IntUnaryOperator y, IntUnaryOperator w, IntUnaryOperator h, String texturePath, int textureXOffset, int textureYOffset) {
        super(app, action, x, y, w, h);
        this.texturePath = texturePath;
        this.textureXOffset = textureXOffset;
        this.textureYOffset = textureYOffset;
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        var texture = getTexture();
        canvas.renderImage(texture, lastX + canvas.scale(textureXOffset), lastY + canvas.scale(textureYOffset));
    }

    public Image getTexture() {
        return isHovered() ? Assets.textureOutlined(texturePath) : Assets.texture(texturePath);
    }
}
