package com.cometkaizo.world;
/**
 * Author: Andy Wang
 * Date Modified: 2026-01-08
 * Description: A 2D cardinal direction
 */
public enum Direction {
    UP(Axis.Y, 0, 1),
    DOWN(Axis.Y, 0, -1),
    LEFT(Axis.X, -1, 0),
    RIGHT(Axis.X, 1, 0);
    private final Axis axis;
    private final int x, y;
    private final Vector.ImmutableInt delta;
    /// Creates a new direction
    Direction(Axis axis, int x, int y) {
        this.axis = axis;
        this.x = x;
        this.y = y;
        this.delta = Vector.immutable(x, y);
    }

    /// Returns the axis that this direction is on
    public Axis axis() {
        return axis;
    }
    /// Returns the opposite direction to this one
    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
    /// Returns the direction associated with the given letter
    public static Direction ofLetter(String letter) {
        return switch (letter.toUpperCase()) {
            case "U" -> UP;
            case "D" -> DOWN;
            case "L" -> LEFT;
            case "R" -> RIGHT;
            default -> throw new IllegalArgumentException(letter);
        };
    }

    /// Returns the delta x value for this direction
    public int x() {
        return x;
    }

    /// Returns the delta y value for this direction
    public int y() {
        return y;
    }

    /// Returns the delta value for this direction
    public Vector.ImmutableInt delta() {
        return delta;
    }
}
