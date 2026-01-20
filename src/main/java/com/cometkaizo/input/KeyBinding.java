package com.cometkaizo.input;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: This class represents a key binding
 */
public class KeyBinding implements InputBinding {
    public final String name;
    public int key;
    public final int defaultKey;
    public boolean isDown;

    /// Creates a new key binding
    public KeyBinding(String name, int defaultKey, int key) {
        this.name = name;
        this.key = key;
        this.defaultKey = defaultKey;
    }
    /// Creates a new key binding
    public KeyBinding(String name, int defaultKey) {
        this(name, defaultKey, defaultKey);
    }

    /// Gets the current key for this key binding
    public int getKey() {
        return key;
    }

    /// Sets the key for this key binding
    public void setKey(int key) {
        this.key = key;
    }

    /// Resets the key for this key binding to the default key
    public void resetKey() {
        this.key = this.defaultKey;
    }

    /// Gets the default key
    public int getDefaultKey() {
        return defaultKey;
    }

    /// Returns whether this key binding is currently active
    @Override
    public boolean isActive() {
        return isDown;
    }

    /// Sets whether this key binding is currently active
    public void setDown(boolean down) {
        isDown = down;
    }

    /// Gets the name of this key binding
    @Override
    public String getName() {
        return name;
    }
}
