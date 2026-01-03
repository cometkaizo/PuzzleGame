package com.cometkaizo.game.item;

public abstract class Item {
    protected abstract String getTexturePathImpl();
    public String getTexturePath() {
        return "gui/item/" + getTexturePathImpl();
    }
}
