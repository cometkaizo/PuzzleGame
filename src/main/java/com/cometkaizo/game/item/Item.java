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
    /// Gets the namespace (id) of this item
    protected abstract String getNamespace();
    /// Gets the path to the texture
    public String getTexturePath() {
        String texturePath = getNamespace();
        if (texturePath == null) return null;
        return "gui/item/" + texturePath;
    }
    /// Gets the texture for this item
    public Image getTexture() {
        String texturePath = getNamespace();
        if (texturePath == null) return null;
        return Assets.texture("gui/item/" + texturePath);
    }

    /// Gets the display name of this item
    public abstract String getName();

    /// Writes this item into a string that can be read in again
    public String write() {
        return new Args(getNamespace(), new String[0]).toString();
    }

    /// An interface for the reading an item in from arguments
    @FunctionalInterface
    public interface Reader {
        Item apply(Args args);
    }
}
