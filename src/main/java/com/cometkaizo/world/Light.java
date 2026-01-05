package com.cometkaizo.world;

import com.cometkaizo.screen.AtlasTexture;
import com.cometkaizo.screen.DirectionAtlasTexture;
import com.cometkaizo.world.block.Block;
import com.cometkaizo.world.entity.Entity;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Light block
 */
public class Light extends Block {
    public Direction direction;

    /**
     * Author: Andy Wang
     * Date Modified: TODO
     * Description: Constructs a light block on the given layer with the given position
     */
    public Light(Room.Layer layer, Vector.Int pos) {
        this(layer, pos, Direction.W);
    }
    /**
     * Author: Andy Wang
     * Date Modified: TODO
     * Description: Constructs a light block on the given layer with the given position and direction
     */
    public Light(Room.Layer layer, Vector.Int pos, Direction direction) {
        super(layer, Vector.immutableInt(pos), new Args("light"));
        this.direction = direction;
    }

    @Override
    public boolean isSolid(Entity entity) {
        return false;
    }
    @Override
    public boolean blocksLight() {
        return false;
    }

    @Override
    protected AtlasTexture getAtlasTexture() {
        return DirectionAtlasTexture.get(direction);
    }
    @Override
    protected String getTexturePath() {
        return "light/1";
    }

    public enum Direction {
        N(0, 1), E(1, 0), S(0, -1), W(-1, 0);
        public final Vector.ImmutableInt delta;

        Direction(int dx, int dy) {
            delta = Vector.immutable(dx, dy);
        }
    }
}
