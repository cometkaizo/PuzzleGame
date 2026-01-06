package com.cometkaizo.game.item;

import com.cometkaizo.screen.Assets;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: This class represents an item that can go into the player's inventory
 */
public abstract class Item {
    protected abstract String getTexturePathImpl();
    public String getTexturePath() {
        String texturePath = getTexturePathImpl();
        if (texturePath == null) return null;
        return "gui/item/" + texturePath;
    }
    public Image getTexture() {
        String texturePath = getTexturePathImpl();
        if (texturePath == null) return null;
        return Assets.texture("gui/item/" + texturePath);
    }

    public abstract String getName();
}
