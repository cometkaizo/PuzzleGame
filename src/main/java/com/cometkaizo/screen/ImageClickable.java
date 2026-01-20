package com.cometkaizo.screen;

import com.cometkaizo.app.GameApp;

import java.awt.*;
import java.util.function.BooleanSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-04
 * Description: A Clickable which has an image that has an outline when hovered
 */
public class ImageClickable extends Clickable {
    protected Supplier<String> texturePath;
    protected int textureXOffset, textureYOffset;

    /// Creates a new clickable
    public ImageClickable(GameApp app, BooleanSupplier action, IntUnaryOperator x, IntUnaryOperator y, IntUnaryOperator w, IntUnaryOperator h, Supplier<String> texturePath, int textureXOffset, int textureYOffset) {
        super(app, action, x, y, w, h);
        this.texturePath = texturePath;
        this.textureXOffset = textureXOffset;
        this.textureYOffset = textureYOffset;
    }

    /// Creates a new clickable
    public ImageClickable(GameApp app, Runnable action, IntUnaryOperator x, IntUnaryOperator y, IntUnaryOperator w, IntUnaryOperator h, Supplier<String> texturePath, int textureXOffset, int textureYOffset) {
        super(app, action, x, y, w, h);
        this.texturePath = texturePath;
        this.textureXOffset = textureXOffset;
        this.textureYOffset = textureYOffset;
    }

    /// Renders this clickable to the screen
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        var texture = getTexture();
        if (texture == null) return;
        canvas.renderImage(texture, lastX + canvas.scale(textureXOffset), lastY + canvas.scale(textureYOffset));
    }

    /// Gets the texture for this clickable
    public Image getTexture() {
        String texturePath = this.texturePath.get();
        if (texturePath == null) return null;
        return isOutlined() ? Assets.textureOutlined(texturePath) : Assets.texture(texturePath);
    }

    /// Returns whether this clickable is outlined
    protected boolean isOutlined() {
        return isHovered();
    }
}
