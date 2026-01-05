package com.cometkaizo.game;

import java.lang.reflect.Field;

public class GameSettings {

    public final int unscaledTileSize = 32;
    public final int renderScale = 4;
    public final int tileSize = unscaledTileSize * renderScale;
    public final double widthInTiles = 10;
    public final double heightInTiles = 6;
    public final double cameraPaddingInTiles = 2;


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
