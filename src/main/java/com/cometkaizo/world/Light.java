package com.cometkaizo.world;

import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.AtlasTexture;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.DirectionAtlasTexture;
import com.cometkaizo.world.block.Block;
import com.cometkaizo.world.entity.CollidableEntity;
import com.cometkaizo.world.entity.Entity;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Light block
 */
public class Light extends Block {
    public Vector.MutableDouble collisionOffset = Vector.mutable(0D, 0D); // the offset to render this light texture at due to collision with an entity
    public Direction direction;
    private final CollidableEntity collisionEntity;

    /**
     * Author: Andy Wang
     * Date Modified: TODO
     * Description: Constructs a light block on the given layer with the given position and direction
     */
    public Light(Room.Layer layer, Vector.Int pos, Direction direction, CollidableEntity collisionEntity) {
        super(layer, Vector.immutableInt(pos), new Args("light"));
        this.direction = direction;
        this.collisionEntity = collisionEntity;
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
    public void render(Canvas canvas) {
        calculateCollisionOffset(canvas);

        var atlas = getTextureAtlas();
        if (atlas == null) return;
        var texture = getAtlasTexture();

        if (shouldRenderBase()) canvas.blitImage(atlas,
                texture.x(), texture.y(), // src
                getX() + collisionOffset.x, getY() + 1 + collisionOffset.y, // dest
                1, 1 // w and h
        );
        if (shouldRenderCollision()) canvas.blitImage(getCollisionTextureAtlas(),
                texture.x(), texture.y(), // src
                getX() + collisionOffset.x, getY() + 1 + collisionOffset.y + getRenderOffsetY(), // dest
                1, 1 // w and h
        );

        canvas.renderDebugBlock(position, Color.YELLOW);
    }

    private double getRenderOffsetY() {
        return direction == Direction.UP ? 0.5 : direction == Direction.DOWN ? -0.4 : 0;
    }

    private boolean shouldRenderBase() {
        return true;
    }
    private boolean shouldRenderCollision() {
        return collisionEntity != null;
    }

    private void calculateCollisionOffset(Canvas canvas) {
        if (collisionEntity == null) this.collisionOffset.set(0D, 0D);
        else {
            var collisionPos = (switch (direction) { // find the position of collision
                case UP -> collisionEntity.getBoundingBox().getBottomCenter();
                case DOWN -> collisionEntity.getBoundingBox().getTopCenter();
                case LEFT -> collisionEntity.getBoundingBox().getRightCenter();
                case RIGHT -> collisionEntity.getBoundingBox().getLeftCenter();
            }).addedTo( // make rendering smoother by using partial tick
                    (collisionEntity.getX() - collisionEntity.getOldX()) * canvas.partialTick(),
                    (collisionEntity.getY() - collisionEntity.getOldY()) * canvas.partialTick()
            );

            this.collisionOffset.set(collisionPos
                    .subtractedBy(position)
                    .with(direction.axis().invert(), 0D));
            // if in positive x or y directions, offset it back by one to render correctly
            if (direction == Direction.UP || direction == Direction.RIGHT) {
                collisionOffset.subtract(direction.delta());
            }

            switch (direction) {
                case UP -> collisionOffset.setY(Math.min(collisionOffset.y, 0));
                case DOWN -> collisionOffset.setY(Math.max(collisionOffset.y, 0));
                case LEFT -> collisionOffset.setX(Math.max(collisionOffset.x, 0));
                case RIGHT -> collisionOffset.setX(Math.min(collisionOffset.x, 0));
            }
        }
    }

    @Override
    public boolean shouldRenderBehindEntities() {
        return collisionEntity != null && direction == Direction.DOWN;
    }

    @Override
    protected AtlasTexture getAtlasTexture() {
        return DirectionAtlasTexture.get(direction);
    }
    @Override
    protected String getTexturePath() {
        return "light/1";
    }
    private Image getCollisionTextureAtlas() {
        return Assets.texture("block/light/2");
    }
}
