package com.cometkaizo.game.item;

import com.cometkaizo.screen.Assets;

import java.awt.*;

public abstract class Item {
    protected abstract String getTexturePath();
    public Image getTexture() {
        String texturePath = getTexturePath();
        if (texturePath == null) return null;
        return Assets.texture("gui/item/" + texturePath);
    }
}
