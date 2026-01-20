package com.cometkaizo.input;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: This class represents an input binding (key or mouse)
 */
public interface InputBinding {
    /// Gets the name of this binding
    String getName();
    /// Returns whether this binding is currently active (pressed)
    boolean isActive();
}
