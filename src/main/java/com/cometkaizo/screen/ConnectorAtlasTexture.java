package com.cometkaizo.screen;

import com.cometkaizo.world.block.Block;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-04
 * Description: Atlas textures which describe the position of the "connected textures" tilemap
 */
public enum ConnectorAtlasTexture implements AtlasTexture {
    _XX_(0, 0, false, true, true, false),
    _XXX(1, 0, false, true, true, true),
    __XX(2, 0, false, false, true, true),
    XXX_(0, 1, true, true, true, false),
    XXXX(1, 1, true, true, true, true),
    X_XX(2, 1, true, false, true, true),
    XX__(0, 2, true, true, false, false),
    XX_X(1, 2, true, true, false, true),
    X__X(2, 2, true, false, false, true),
    X_X_(0, 5, true, false, true, false),
    ____(0, 6, false, false, false, false),
    _X_X(1, 6, false, true, false, true),
    _X__(0, 10, false, true, false, false),
    ___X(1, 10, false, false, false, true),
    __X_(2, 9, false, false, true, false),
    X___(2, 10, true, false, false, false);

    private static final ConnectorAtlasTexture[][][][] ALL = new ConnectorAtlasTexture[2][2][2][2];
    static {
        for (var val : values()) {
            ALL[val.n?1:0][val.e?1:0][val.s?1:0][val.w?1:0] = val;
        }
    }
    /// Gets the connected atlas texture associated with the given connections
    public static ConnectorAtlasTexture get(boolean n, boolean e, boolean s, boolean w) {
        return ALL[n?1:0][e?1:0][s?1:0][w?1:0];
    }

    public final double x, y;
    public final boolean n, e, s, w;

    /// Creates a new connected atlas texture
    ConnectorAtlasTexture(double x, double y, boolean n, boolean e, boolean s, boolean w) {
        this.x = x;
        this.y = y;
        this.n = n;
        this.e = e;
        this.s = s;
        this.w = w;
    }

    /// Gets the connected atlas texture associated with the connection state of the given block
    public static AtlasTexture get(Block block) {
        return get(block.isConnectedN(), block.isConnectedE(), block.isConnectedS(), block.isConnectedW());
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
        return !s;
    }
}
