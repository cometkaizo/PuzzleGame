package com.cometkaizo.input;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: This class represents a mouse button binding
 */
public class MouseButtonBinding implements InputBinding {
    public final String name;
    public int button;
    public final int defaultButton;
    public boolean isDown;

    /// Creates a new mouse binding
    public MouseButtonBinding(String name, int defaultButton, int button) {
        this.name = name;
        this.button = button;
        this.defaultButton = defaultButton;
    }
    /// Creates a new mouse binding
    public MouseButtonBinding(String name, int defaultButton) {
        this(name, defaultButton, defaultButton);
    }

    /// Gets the button for this binding
    public int getButton() {
        return button;
    }
    /// Sets the button for this binding
    public void setButton(int button) {
        this.button = button;
    }
    /// Resets the button to the default button
    public void resetButton() {
        this.button = this.defaultButton;
    }
    /// Gets the default button
    public int getDefaultButton() {
        return defaultButton;
    }

    /// Returns whether this binding is currently active
    @Override
    public boolean isActive() {
        return isDown;
    }

    /// Sets whether this binding is currently active
    public void setDown(boolean down) {
        isDown = down;
    }

    /// Gets the name of this binding
    @Override
    public String getName() {
        return name;
    }
}
