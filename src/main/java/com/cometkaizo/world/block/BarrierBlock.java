package com.cometkaizo.world.block;

import com.cometkaizo.screen.Canvas;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
import com.cometkaizo.world.entity.Entity;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-29
 * Description: Invisible solid block
 */
public class BarrierBlock extends Block {

    /// Creates a new block
    public BarrierBlock(Room.Layer layer, Vector.ImmutableInt position, Args args) {
        super(layer, position, args);
    }

    /// Returns whether this block is solid
    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    /// Renders this block to the screen
    @Override
    public void render(Canvas canvas) {
        canvas.renderDebugBlock(position, Color.RED);
    }
    /// Gets the path to the texture atlas for this block
    @Override
    protected String getTexturePath() {
        return null;
    }
}
