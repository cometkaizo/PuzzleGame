package com.cometkaizo.screen;

import com.cometkaizo.world.Direction;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-08
 * Description: Textures describing the "directional textures" tilemap
 */
public enum DirectionAtlasTexture implements AtlasTexture {
    N(1, 0), NE(2, 0), E(2, 1), SE(2, 2), S(1, 2), SW(0, 2), W(0, 1), NW(0, 0);

    public final double x, y;

    /// Creates a new atlas texture
    DirectionAtlasTexture(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /// Gets the atlas texture associated with the given direction
    public static DirectionAtlasTexture get(Direction direction) {
        return switch (direction) {
            case UP -> N;
            case RIGHT -> E;
            case DOWN -> S;
            case LEFT -> W;
        };
    }

    /// The x position of this texture in the atlas in blocks
    @Override
    public double x() {
        return x;
    }

    /// The y position of this texture in the atlas in blocks
    @Override
    public double y() {
        return y;
    }

    /// Returns true if this texture extends downwards for the full height, and false otherwise.
    /// This is useful for making textures for blocks that have block states where some
    /// other block is obscuring part of the texture and so that part of the texture can
    /// be omitted from the texture atlas to minimize space
    @Override
    public boolean hasYExtension() {
        return false;
    }
}
