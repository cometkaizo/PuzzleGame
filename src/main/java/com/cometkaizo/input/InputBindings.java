package com.cometkaizo.input;

import com.cometkaizo.registry.Registry;

import java.util.function.Supplier;

import static java.awt.event.KeyEvent.*;
import static java.awt.event.MouseEvent.BUTTON1;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Lists all input bindings
 */
public class InputBindings {

    public static final Registry<InputBinding> GAME = new Registry<>();
    public static final Registry<InputBinding> OVERLAY = new Registry<>();

    public static final Supplier<KeyBinding> JUMP = GAME.register("jump", app -> new KeyBinding("Jump", VK_SPACE));
    public static final Supplier<KeyBinding> RIGHT = GAME.register("right", app -> new KeyBinding("Right", VK_D));
    public static final Supplier<KeyBinding> LEFT = GAME.register("left", app -> new KeyBinding("Left", VK_A));
    public static final Supplier<KeyBinding> UP = GAME.register("up", app -> new KeyBinding("Up", VK_W));
    public static final Supplier<KeyBinding> DOWN = GAME.register("down", app -> new KeyBinding("Down", VK_S));
    public static final Supplier<KeyBinding> INTERACT = GAME.register("interact", app -> new KeyBinding("Interact", VK_E));
    public static final Supplier<KeyBinding> SOLVE = GAME.register("solve", app -> new KeyBinding("solve", VK_V));
    public static final Supplier<KeyBinding> TOGGLE_DEBUG = GAME.register("toggle_debug", app -> new KeyBinding("Toggle Debug", VK_Z));

    public static final Supplier<KeyBinding> OVERLAY_CLOSE = OVERLAY.register("overlay_close", app -> new KeyBinding("Close Overlay", VK_E));
    public static final Supplier<MouseButtonBinding> OVERLAY_INTERACT = OVERLAY.register("overlay_interact", app -> new MouseButtonBinding("Interact Overlay", BUTTON1));

}
