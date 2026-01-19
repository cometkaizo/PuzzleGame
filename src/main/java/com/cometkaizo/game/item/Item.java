package com.cometkaizo.game.item;

import com.cometkaizo.screen.Assets;
import com.cometkaizo.world.Args;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: This class represents an item that can go into the player's inventory
 */
public abstract class Item {
    protected String getTexturePathImpl() {
        return getNamespace();
    }
    protected abstract String getNamespace();
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

    public String write() {
        return new Args(getNamespace(), new String[0]).toString();
    }

    public interface Reader {
        Item apply(Args args);
    }
}
