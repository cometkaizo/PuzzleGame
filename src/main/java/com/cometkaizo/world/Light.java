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
 * Date Modified: 2026-01-15
 * Description: Light block
 */
public class Light extends Block {
    public Vector.MutableDouble collisionOffset = Vector.mutable(0D, 0D); // the offset to render this light texture at due to collision with an entity
    public Direction direction;
    private final CollidableEntity collisionEntity;
    private final boolean first;

    /**
     * Constructs a light block on the given layer with the given position and direction
     */
    public Light(Room.Layer layer, Vector.Int pos, Direction direction, CollidableEntity collisionEntity, boolean first) {
        super(layer, Vector.immutableInt(pos), new Args("light"));
        this.direction = direction;
        this.collisionEntity = collisionEntity;
        this.first = first;
    }

    /// Returns whether this block is solid
    @Override
    public boolean isSolid(Entity entity) {
        return false;
    }
    /// Returns whether this block stops light from passing through it
    @Override
    public boolean blocksLight() {
        return false;
    }

    /// Renders this light to the screen
    @Override
    public void render(Canvas canvas) {
        calculateCollisionOffset(canvas);

        var atlas = getTextureAtlas();
        if (atlas == null) return;
        var texture = getAtlasTexture();

        var oldClip = canvas.getGraphics().getClip();
        applyClip(canvas);

        if (shouldRenderBase()) renderBase(canvas, atlas, texture);
        if (shouldRenderCollision()) renderCollision(canvas, texture);

        canvas.getGraphics().setClip(oldClip);

        canvas.renderDebugBlock(position, Color.YELLOW);
    }

    /// Clips the canvas to prevent drawing outside the block weirdly
    private void applyClip(Canvas canvas) {
        // clip the drawing area so that we don't draw outside the block in weird ways
        // the first light block in a ray of light has about half the drawing area as other light blocks, since it starts in the middle
        canvas.getGraphics().setClip(
                direction == Direction.RIGHT ? canvas.toScreenX(position.x - getRenderOffsetX()) : 0,
                direction == Direction.DOWN ? canvas.toScreenY(position.y + (first ? 0.5 : 1) - getRenderOffsetY()) : 0,
                direction == Direction.LEFT ? canvas.toScreenX(position.x + (first ? 0.3 : 1) - getRenderOffsetX()) : canvas.getWidth(),
                direction == Direction.UP ? canvas.toScreenY(position.y + (first ? 1 : 0) - getRenderOffsetY()) : canvas.getHeight()
        );
    }

    /// Renders the base light texture
    private void renderBase(Canvas canvas, Image atlas, AtlasTexture texture) {
        canvas.blitImage(atlas,
                texture.x(), texture.y(), // src
                getX(), getY() + 1, // dest
                1, 1 // w and h
        );
    }

    /// Renders the "collision" light texture
    private void renderCollision(Canvas canvas, AtlasTexture texture) {
        canvas.blitImage(getCollisionTextureAtlas(),
                texture.x(), texture.y(), // src
                getX() + collisionOffset.x + getRenderOffsetX(), getY() + 1 + collisionOffset.y + getRenderOffsetY(), // dest
                1, 1 // w and h
        );
    }

    /// Gets the y render offset
    private double getRenderOffsetY() {
        return direction == Direction.UP ? 0.5 : direction == Direction.DOWN ? -0.4 : 0;
    }
    /// Gets the x render offset
    private double getRenderOffsetX() {
        return direction == Direction.LEFT ? -0.2 : direction == Direction.RIGHT ? 0 : 0;
    }

    /// Returns whether the base light texture should be rendered
    private boolean shouldRenderBase() {
        return collisionEntity == null || switch (direction) {
            case UP -> collisionOffset.y + getRenderOffsetY() > 0;
            case DOWN -> collisionOffset.y + getRenderOffsetY() < 0;
            case LEFT -> collisionOffset.x + getRenderOffsetX() < 0;
            case RIGHT -> collisionOffset.x + getRenderOffsetX() > 0;
        };
    }
    /// Returns whether the "collision" light texture should be rendered
    private boolean shouldRenderCollision() {
        return collisionEntity != null;
    }

    /// Calculates the offset to the "collision" light texture caused by the collided entity
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
            // if in positive x or y directions, offset it back by a bit to render correctly
            if (direction == Direction.UP || direction == Direction.RIGHT) {
                collisionOffset.subtract(Vector.immutableDouble(direction.delta()).scaledBy(0.8));
            }
        }
    }

    /// Returns whether this block should render behind other entities in the same y interval
    @Override
    public boolean shouldRenderBehindEntities() {
        return collisionEntity != null && direction == Direction.DOWN;
    }

    /// Gets the atlas texture for this block
    @Override
    protected AtlasTexture getAtlasTexture() {
        return DirectionAtlasTexture.get(direction);
    }
    /// Gets the path to the texture atlas for this block
    @Override
    protected String getTexturePath() {
        return "light/" + (first ? "3" : "1");
    }
    /// Returns the texture atlas for the "collision" light textures
    private Image getCollisionTextureAtlas() {
        return Assets.texture("block/light/2");
    }
}
