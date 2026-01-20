package com.cometkaizo.world.block;

import com.cometkaizo.screen.AtlasTexture;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.ConnectorAtlasTexture;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
import com.cometkaizo.world.entity.Entity;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-16
 * Description: Counter block (e.g., for the info desk)
 */
public class CounterBlock extends Block {

    protected String textureVariation;

    /// Creates a new block
    public CounterBlock(Room.Layer layer, Vector.ImmutableInt position, Args args) {
        super(layer, position, args);
    }

    /// Returns whether this block is solid
    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    @Override
    public void reset() {
        super.reset();
        textureVariation = originalArgs.next("1");
    }

    /// Renders this block to the screen
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderDebugBlock(position, Color.RED);
    }

    /// Gets the path to the texture atlas for this block
    @Override
    protected String getTexturePath() {
        return "counter/" + textureVariation;
    }

    /// Gets the atlas texture for this block
    @Override
    protected AtlasTexture getAtlasTexture() {
        return ConnectorAtlasTexture.get(this);
    }

    /// Returns the height of this block
    @Override
    protected double getTextureHeight() {
        return 1.5;
    }
}
