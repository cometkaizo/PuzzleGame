package com.cometkaizo.world;
/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: A 2D axis
 */
public enum Axis {
    X,
    Y;

    public Axis invert() {
        return this == X ? Y : X;
    }
}
