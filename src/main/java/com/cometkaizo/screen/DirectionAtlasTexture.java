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

    DirectionAtlasTexture(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static DirectionAtlasTexture get(Direction direction) {
        return switch (direction) {
            case UP -> N;
            case RIGHT -> E;
            case DOWN -> S;
            case LEFT -> W;
        };
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public boolean hasYExtension() {
        return false;
    }
}
