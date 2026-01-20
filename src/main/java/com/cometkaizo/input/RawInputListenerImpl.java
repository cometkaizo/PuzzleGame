package com.cometkaizo.input;

import com.cometkaizo.registry.Registry;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-16
 * Description: Simple implementation for RawInputListener
 */
public class RawInputListenerImpl implements RawInputListener {
    private final Set<InputListener> inputListeners = new HashSet<>(1);
    private final Registry<InputBinding> keyBindings;
    private final BooleanSupplier activeCondition;
    private boolean prevInactive;
    private int mouseX, mouseY;

    /// Creates a new input listener
    public RawInputListenerImpl(Registry<InputBinding> keyBindings, BooleanSupplier activeCondition) {
        this.keyBindings = keyBindings;
        this.activeCondition = activeCondition;
    }

    /// Adds an input listener
    @Override
    public void addInputListener(InputListener listener) {
        inputListeners.add(listener);
    }
    /// Removes an input listener
    @Override
    public void removeInputListener(InputListener listener) {
        inputListeners.remove(listener);
    }

    /// Called when a key is typed
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /// Updates all key bindings when a key is pressed
    @Override
    public void keyPressed(KeyEvent e) {
        if (inactive()) return;
        keyBindings.values().forEach(binding -> {
            if (binding instanceof KeyBinding keyBinding &&
                    e.getKeyCode() == keyBinding.key) {
                boolean prevIsDown = keyBinding.isDown;
                keyBinding.isDown = true;

                // rising edge of the key
                if (!prevIsDown) inputListeners.forEach(l -> l.keyPressed(keyBinding));
            }
        });
    }

    /// Updates all key bindings when a key is released
    @Override
    public void keyReleased(KeyEvent e) {
//        if (inactive()) return; // don't block "release" events when inactive
        keyBindings.values().forEach(binding -> {
            if (binding instanceof KeyBinding keyBinding && e.getKeyCode() == keyBinding.key) {
                keyBinding.isDown = false;
                inputListeners.forEach(l -> l.keyReleased(keyBinding));
            }
        });
    }

    /// Called when the mouse is clicked
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /// Updates all mouse bindings when a mouse button is pressed
    @Override
    public void mousePressed(MouseEvent e) {
        if (inactive()) return;
        keyBindings.values().forEach(binding -> {
            if (binding instanceof MouseButtonBinding buttonBinding &&
                    e.getButton() == buttonBinding.button) {
                boolean prevIsDown = buttonBinding.isDown;
                buttonBinding.isDown = true;

                // rising edge of the mouse
                if (!prevIsDown) inputListeners.forEach(l -> l.mousePressed(buttonBinding, e.getX(), e.getY()));
            }
        });
    }

    /// Updates all mouse bindings when a mouse button is released
    @Override
    public void mouseReleased(MouseEvent e) {
//        if (inactive()) return; // don't block "release" events when inactive
        keyBindings.values().forEach(binding -> {
            if (binding instanceof MouseButtonBinding buttonBinding && e.getButton() == buttonBinding.button) {
                buttonBinding.isDown = false;
                inputListeners.forEach(l -> l.mouseReleased(buttonBinding, e.getX(), e.getY()));
            }
        });
    }

    /// Called when the mouse enters something
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /// Called when the mouse exits something
    @Override
    public void mouseExited(MouseEvent e) {

    }

    /// Called when the mouse is dragged
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /// Updates the mouse position when it is moved
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        if (prevInactive) return;
        inputListeners.forEach(l -> l.mouseMoved(e.getX(), e.getY()));
    }

    /// Returns whether this input listener is currently inactive
    private boolean inactive() {
        return !activeCondition.getAsBoolean();
    }

    /// Called every tick
    @Override
    public void tick() {
        if (inactive() && !prevInactive) interruptHeldKeys();
        else tickHeldKeys();
        prevInactive = inactive();
    }

    /// Auto-releases key and mouse bindings if this input listener becomes inactive in the middle of a key or mouse hold
    private void interruptHeldKeys() {
        keyBindings.values().forEach(binding -> {
            if (binding instanceof KeyBinding keyBinding && keyBinding.isDown) {
                keyBinding.isDown = false;
                inputListeners.forEach(l -> l.keyReleased(keyBinding));
            } else if (binding instanceof MouseButtonBinding buttonBinding && buttonBinding.isDown) {
                buttonBinding.isDown = false;
                inputListeners.forEach(l -> l.mouseReleased(buttonBinding, mouseX, mouseY));
            }
        });
    }

    /// Updates all the held keys
    private void tickHeldKeys() {
        keyBindings.values().forEach(binding -> {
            if (binding instanceof KeyBinding keyBinding && keyBinding.isDown) {
                // holding of the key
                inputListeners.forEach(l -> l.keyDown(keyBinding));
            } else if (binding instanceof MouseButtonBinding buttonBinding && buttonBinding.isDown) {
                // holding of the mouse
                inputListeners.forEach(l -> l.mouseDown(buttonBinding, mouseX, mouseY));
            }
        });
    }
}
