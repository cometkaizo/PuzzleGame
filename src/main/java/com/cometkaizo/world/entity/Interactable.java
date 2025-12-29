package com.cometkaizo.world.entity;

import com.cometkaizo.game.event.KeyPressedEvent;
import com.cometkaizo.input.InputBindings;
import com.cometkaizo.input.KeyBinding;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;

public abstract class Interactable extends CollidableEntity {
    public Interactable(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        eventBus.register(KeyPressedEvent.class, this::onKeyPressed);
    }

    private void onKeyPressed(KeyPressedEvent event) {
        KeyBinding input = event.input();
        if (input == InputBindings.INTERACT.get() && canBeInteracted()) {
            interact();
            room.player.onInteract();
        }
    }

    protected boolean canBeInteracted() {
        return isTouching(room.player, interactDistance()) && room.player.canInteract();
    }

    protected abstract void interact();

    protected double interactDistance() {
        return 0.1;
    }
    protected Image getTexture() {
        String texturePath = getTexturePath();
        if (texturePath == null) return null;
        return canBeInteracted() ? Assets.textureOutlined("entity/" + texturePath) : Assets.texture("entity/" + texturePath);
    }
}
