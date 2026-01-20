package com.cometkaizo.world.entity;

import com.cometkaizo.world.Vector;
/**
 * Author: Andy Wang
 * Date Modified: 2026-01-08
 * Description: Describes the 2D rectangular bounding box of an entity
 */
public class BoundingBox {
    public Vector.MutableDouble position; // bottom left corner
    public Vector.ImmutableDouble size;

    /// Creates a new bounding box
    public BoundingBox(Vector.MutableDouble position, Vector.ImmutableDouble size) {
        this.position = position;
        this.size = size;
    }

    /// Gets the width
    public double getWidth() {
        return size.x;
    }
    /// Gets the height
    public double getHeight() {
        return size.y;
    }
    /// Gets the x position
    public double getX() {
        return position.x;
    }
    /// Gets the y position
    public double getY() {
        return position.y;
    }

    /// Gets the top y position
    public double getTop() {
        return getY() + getHeight();
    }
    /// Gets the bottom y position
    public double getBottom() {
        return getY();
    }
    /// Gets the left x position
    public double getLeft() {
        return getX();
    }
    /// Gets the right x position
    public double getRight() {
        return getX() + getWidth();
    }

    /// Gets the center x position
    public double getCenterX() {
        return getX() + getWidth() / 2;
    }
    /// Gets the center y position
    public double getCenterY() {
        return getY() + getHeight() / 2;
    }

    /// Gets the top center position
    public Vector.Double getTopCenter() {
        return Vector.immutable(getCenterX(), getTop());
    }
    /// Gets the bottom center position
    public Vector.Double getBottomCenter() {
        return Vector.immutable(getCenterX(), getBottom());
    }
    /// Gets the left center position
    public Vector.Double getLeftCenter() {
        return Vector.immutable(getLeft(), getCenterY());
    }
    /// Gets the right center position
    public Vector.Double getRightCenter() {
        return Vector.immutable(getRight(), getCenterY());
    }

    /// Returns whether this bounding box intersects another.
    /// This method takes into account floating point inaccuracies.
    public boolean intersects(BoundingBox other) {
        return getLeft() < other.getRight() - 1E-7 &&
                other.getLeft() < getRight() - 1E-7 &&
                getBottom() < other.getTop() - 1E-7 &&
                other.getBottom() < getTop() - 1E-7;
    }

    /// Returns whether this bounding box contains the given position.
    /// This method takes into account floating point inaccuracies.
    public boolean contains(Vector.Double position) {
        return position.getX() > getLeft() + 1E-7 && position.getX() < getRight() - 1E-7 &&
                position.getY() > getBottom() + 1E-7 && position.getY() < getTop() - 1E-7;
    }

    /// Creates a copy of this bounding box
    public BoundingBox copy() {
        return new BoundingBox(Vector.mutableDouble(position), size);
    }

    /// Expands a copy of this bounding box expanded by the given amount
    public BoundingBox expanded(double amount) {
        return new BoundingBox(Vector.mutable(getLeft() - amount, getBottom() - amount),
                Vector.immutable(getWidth() + amount * 2, getHeight() + amount * 2));
    }
    /// Expands a copy of this bounding box by the given amounts in each direction
    public BoundingBox expanded(double u, double r, double d, double l) {
        return new BoundingBox(Vector.mutable(getLeft() - l, getBottom() - d),
                Vector.immutable(getWidth() + (r + l), getHeight() + (d + u)));
    }

    /// Turns this bounding box into a string
    @Override
    public String toString() {
        return "(" + getLeft() + ", " + getBottom() + ") -> (" + getRight() + ", " + getTop() + ")";
    }
}
