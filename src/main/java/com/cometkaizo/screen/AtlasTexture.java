package com.cometkaizo.screen;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-29
 * Description: A single texture within a texture atlas (like a tilemap)
 */
public interface AtlasTexture {
    /// Returns the x position of this texture in the atlas, measured in blocks
    double x();
    /// Returns the y position of this texture in the atlas, measured in blocks
    double y();
    /// Returns true if this texture extends downwards for the full height, and false otherwise.
    /// This is useful for making textures for blocks that have block states where some
    /// other block is obscuring part of the texture and so that part of the texture can
    /// be omitted from the texture atlas to minimize space
    boolean hasYExtension();

    /// The default atlas texture for just a single block state (32 x 32 texture)
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
