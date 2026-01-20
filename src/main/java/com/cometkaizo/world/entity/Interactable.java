package com.cometkaizo.world.entity;

import com.cometkaizo.game.event.KeyPressedEvent;
import com.cometkaizo.input.InputBindings;
import com.cometkaizo.input.KeyBinding;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.world.Args;
import com.cometkaizo.world.Room;
import com.cometkaizo.world.Vector;

import java.awt.*;
/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: An entity which does something when interacted with by the player
 */
public abstract class Interactable extends CollidableEntity {
    /// Creates a new interactable entity
    public Interactable(Room.Layer layer, Vector.MutableDouble position, Args args) {
        super(layer, position, args);
        eventBus.register(KeyPressedEvent.class, this::onKeyPressed);
    }

    /// Possibly performs an action when a key is pressed
    private void onKeyPressed(KeyPressedEvent event) {
        KeyBinding input = event.input();
        if (canBeInteracted()) {
            if (input == InputBindings.INTERACT.get()) {
                interact();
                room.player.onInteract();
            } else if (input == InputBindings.SOLVE.get() && game.isDevMode()) {
                solve();
                room.player.onInteract();
            }
        }
    }

    /// Returns whether this entity can be interacted
    protected boolean canBeInteracted() {
        return isTouching(room.player, interactDistance()) && room.player.canInteract();
    }

    /// Performs an action when the player interacts with this entity
    protected abstract void interact();
    /// Forcibly solves this interactable
    protected void solve() {

    }

    /// Returns the distance that the player must be in order to interact with this entity
    protected double interactDistance() {
        return 0.1;
    }
    /// Gets this entity's texture
    protected Image getTexture() {
        String texturePath = getTexturePath();
        if (texturePath == null) return null;
        return canBeInteracted() ? Assets.textureOutlined("entity/" + texturePath) : Assets.texture("entity/" + texturePath);
    }
}
