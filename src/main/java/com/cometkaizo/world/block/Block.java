package com.cometkaizo.world.block;

import com.cometkaizo.io.DataSerializable;
import com.cometkaizo.io.data.CompoundData;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.AtlasTexture;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Renderable;
import com.cometkaizo.world.*;
import com.cometkaizo.world.entity.Entity;

import java.awt.*;

public abstract class Block implements Renderable, DataSerializable, Resettable {
    public static final String TYPE_KEY = "type";
    public static final String POSITION_KEY = "position";
    public final Room room;
    public final Room.Layer layer;
    protected final Args originalArgs;
    public String name;
    public final Vector.ImmutableInt position;
    private Boolean connectedN, connectedE, connectedS, connectedW;

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

    public abstract boolean isSolid(Entity entity);

    @Override
    public void render(Canvas canvas) {
        var atlas = getTextureAtlas();
        if (atlas == null) return;
        var texture = getAtlasTexture();
        canvas.blitImage(atlas,
                texture.x(), texture.y(), // src
                getX(), getY() + getHeight(), // dest
                1, texture.hasYExtension() ? getHeight() : 1 // w and h
        );
    }

    protected abstract String getTexturePath();
    private Image getTextureAtlas() {
        var texturePath = getTexturePath();
        if (texturePath == null) return null;
        return Assets.texture("block/" + texturePath);
    }
    protected double getHeight() {
        return 2.5;
    }
    protected AtlasTexture getAtlasTexture() {
        return AtlasTexture.SINGLE_BLOCK;
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

    public interface Reader {
        Block apply(Room.Layer layer, Vector.ImmutableInt pos, Args args);
    }

    @Override
    public CompoundData write() {
        return null;
    }

    @Override
    public void read(CompoundData data) {

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
