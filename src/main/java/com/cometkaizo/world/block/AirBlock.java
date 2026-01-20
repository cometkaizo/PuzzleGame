package com.cometkaizo.world.block;

import com.cometkaizo.screen.Canvas;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
import com.cometkaizo.world.entity.Entity;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-04
 * Description: Air block
 */
public class AirBlock extends Block {

    /// Creates a new air block
    public AirBlock(Room.Layer layer, Vector.ImmutableInt position, Args args) {
        super(layer, position, args);
    }

    /// Returns whether this block is solid
    @Override
    public boolean isSolid(Entity entity) {
        return false;
    }

    /// Renders this block to the screen
    @Override
    public void render(Canvas canvas) {
        // do nothing because air blocks are invisble
    }

    /// Returns whether this block blocks light
    @Override
    public boolean blocksLight() {
        return false;
    }

    /// Gets the path to the texture atlas for this block
    @Override
    protected String getTexturePath() {
        return null;
    }

}
