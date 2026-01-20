package com.cometkaizo.world;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: An object which can be ticked, or updated, many times a second
 */
public interface Tickable {
    /// Updates this object every tick
    void tick();
}
