package com.cometkaizo.game;

import java.lang.reflect.Field;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Settings for the game
 */
public class GameSettings {

    public int unscaledTileSize = 32;
    public int renderScale = 4;
    public int tileSize = unscaledTileSize * renderScale;
    public double widthInTiles = 10;
    public double heightInTiles = 6;
    public double cameraPaddingInTiles = 2;


    public String toString() {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(getClass().getSimpleName());
            builder.append('{');

            for (Field field : getClass().getFields()) {
                builder.append("\n\t")
                        .append(field.getName())
                        .append(": ")
                        .append(field.get(this));
            }

            if (getClass().getFields().length > 0)
                builder.append('\n');
            builder.append('}');
            return builder.toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
