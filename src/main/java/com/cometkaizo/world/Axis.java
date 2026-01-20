package com.cometkaizo.world;
/**
 * Author: Andy Wang
 * Date Modified: 2026-01-04
 * Description: A 2D axis
 */
public enum Axis {
    X,
    Y;

    /// Returns the other axis
    public Axis invert() {
        return this == X ? Y : X;
    }
}
