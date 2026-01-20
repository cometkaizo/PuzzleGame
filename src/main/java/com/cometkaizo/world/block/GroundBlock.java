package com.cometkaizo.world.block;

import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
import com.cometkaizo.world.entity.Entity;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-19
 * Description: Ground block
 */
public class GroundBlock extends Block {

    /// Creates a new block
    public GroundBlock(Room.Layer layer, Vector.ImmutableInt position, Args args) {
        super(layer, position, args);
    }

    /// Returns whether this block is solid
    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    /// Gets the path to the texture atlas for this block
    @Override
    protected String getTexturePath() {
        return "ground/1";
    }

}
