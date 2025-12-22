package com.cometkaizo.input;

import com.cometkaizo.registry.Registry;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;

public class RawInputListenerImpl implements RawInputListener {
    private final Set<InputListener> inputListeners = new HashSet<>(1);
    private final Registry<InputBinding> keyBindings;
    private final BooleanSupplier activeCondition;
    private boolean prevInactive;
    private int mouseX, mouseY;

    public RawInputListenerImpl(Registry<InputBinding> keyBindings, BooleanSupplier activeCondition) {
        this.keyBindings = keyBindings;
        this.activeCondition = activeCondition;
    }

    @Override
    public void addInputListener(InputListener listener) {
        inputListeners.add(listener);
    }
    @Override
    public void removeInputListener(InputListener listener) {
        inputListeners.remove(listener);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (prevInactive) return;
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

    @Override
    public void keyReleased(KeyEvent e) {
        if (prevInactive) return;
        keyBindings.values().forEach(binding -> {
            if (binding instanceof KeyBinding keyBinding && e.getKeyCode() == keyBinding.key) {
                keyBinding.isDown = false;
                inputListeners.forEach(l -> l.keyReleased(keyBinding));
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (prevInactive) return;
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

    @Override
    public void mouseReleased(MouseEvent e) {
        if (prevInactive) return;
        keyBindings.values().forEach(binding -> {
            if (binding instanceof MouseButtonBinding buttonBinding && e.getButton() == buttonBinding.button) {
                buttonBinding.isDown = false;
                inputListeners.forEach(l -> l.mouseReleased(buttonBinding, e.getX(), e.getY()));
            }
        });
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    private boolean inactive() {
        return !activeCondition.getAsBoolean();
    }

    @Override
    public void tick() {
        if (inactive() && !prevInactive) interruptHeldKeys();
        tickHeldKeys();
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
