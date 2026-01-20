package com.cometkaizo.world.block;

import com.cometkaizo.game.GameState;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.AtlasTexture;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Renderable;
import com.cometkaizo.world.*;
import com.cometkaizo.world.entity.Entity;

import java.awt.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: A single block in the world map
 */
public abstract class Block implements Renderable, Resettable {
    public final Room room;
    public final Room.Layer layer;
    protected final Args originalArgs;
    public String name;
    public final Vector.ImmutableInt position;
    private Boolean connectedN, connectedE, connectedS, connectedW;
    protected boolean lit;

    /// Creates a new block
    public Block(Room.Layer layer, Vector.ImmutableInt position, Args args) {
        this.room = layer.room;
        this.layer = layer;
        this.position = position;
        this.originalArgs = args;
        reset();
    }

    @Override
    public void reset() {
        originalArgs.reset();
        this.name = originalArgs.next();
    }

    /// Returns whether this block is solid
    public abstract boolean isSolid(Entity entity);

    public void resetLight() {
        lit = false;
    }
    public void tickLightEmission() {

    }

    @Override
    public void render(Canvas canvas) {
        var atlas = getTextureAtlas();
        if (atlas == null) return;
        var texture = getAtlasTexture();
        canvas.blitImage(atlas,
                texture.x(), texture.y(), // src
                getX(), getY() + getTextureHeight(), // dest
                1, texture.hasYExtension() ? getTextureHeight() : 1 // w and h
        );
    }

    /// Gets the path to the texture atlas for this block
    protected abstract String getTexturePath();
    /// Gets the texture atlas for this block
    protected Image getTextureAtlas() {
        var texturePath = getTexturePath();
        if (texturePath == null) return null;
        return Assets.texture("block/" + texturePath);
    }
    /// Returns the height of this block
    protected double getTextureHeight() {
        return 1;
    }
    /// Gets the atlas texture for this block
    protected AtlasTexture getAtlasTexture() {
        return AtlasTexture.SINGLE_BLOCK;
    }

    /**
     * Author: Andy Wang
     * Date Modified: 2026-01-08
     * Description: returns whether this block should be rendered behind entities at its y-level
     */
    public boolean shouldRenderBehindEntities() {
        return false;
    }

    public String getNamespace() {
        return "";
    }

    public Vector.ImmutableInt getPosition() {
        return position;
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public boolean hasName() {
        return name != null && !name.isBlank();
    }
    public String getName() {
        return name;
    }


    /// Returns whether this block blocks light
    public boolean blocksLight() {
        return true;
    }
    public void updateLight(Direction direction) {
        lit = direction != null;
    }

    public void write(GameState state) {

    }

    /// Functional interface for reading in a block
    @FunctionalInterface
    public interface Reader {
        Block apply(Room.Layer layer, Vector.ImmutableInt pos, Args args);
    }

    public boolean isConnectedN() {
        if (connectedN == null) {
            connectedN = layer.getBlockType(getX(), getY() + 1).orElse(null) == getClass();
        }
        return connectedN;
    }
    public boolean isConnectedE() {
        if (connectedE == null) {
            connectedE = layer.getBlockType(getX() + 1, getY()).orElse(null) == getClass();
        }
        return connectedE;
    }
    public boolean isConnectedS() {
        if (connectedS == null) {
            connectedS = layer.getBlockType(getX(), getY() - 1).orElse(null) == getClass();
        }
        return connectedS;
    }
    public boolean isConnectedW() {
        if (connectedW == null) {
            connectedW = layer.getBlockType(getX() - 1, getY()).orElse(null) == getClass();
        }
        return connectedW;
    }
}
