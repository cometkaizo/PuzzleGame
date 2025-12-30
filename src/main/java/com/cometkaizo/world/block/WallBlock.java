package com.cometkaizo.world.block;

import com.cometkaizo.screen.AtlasTexture;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.ConnectorAtlasTexture;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;
import com.cometkaizo.world.entity.Entity;

import java.awt.*;

public class WallBlock extends Block {

    protected String textureVariation;

    public WallBlock(Room.Layer layer, Vector.ImmutableInt position, Args args) {
        super(layer, position, args);
    }

    @Override
    public boolean isSolid(Entity entity) {
        return true;
    }

    @Override
    public void reset() {
        super.reset();
        textureVariation = originalArgs.next(null);
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderDebugBlock(position, Color.RED);
    }

    @Override
    protected String getTexturePath() {
        return "wall/" + ((isConnectedE() && isConnectedW() && !isConnectedS() && getX()%2 == 0) ? "2" : "1");
    }

    @Override
    protected AtlasTexture getAtlasTexture() {
        return ConnectorAtlasTexture.get(this);
    }

    @Override
    protected double getHeight() {
        return 2.5;
    }
}
