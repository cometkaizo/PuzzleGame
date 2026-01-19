package com.cometkaizo.screen;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-29
 * Description: A single texture within a texture atlas (like a tilemap)
 */
public interface AtlasTexture {
    double x();
    double y();
    boolean hasYExtension();
    AtlasTexture SINGLE_BLOCK = new AtlasTexture() {
        @Override public double x() {
            return 0;
        }
        @Override public double y() {
            return 0;
        }
        @Override public boolean hasYExtension() {
            return false;
        }
    };
}
