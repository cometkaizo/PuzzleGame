package com.cometkaizo.world;

import com.cometkaizo.util.MathUtils;

import java.util.Objects;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-08
 * Description: A 2D (x, y) pair. Can be any number type, usually ints and doubles.
 */
@SuppressWarnings("unused")
public interface Vector<T extends Number> {

    /// Gets the horizontal component of this vector
    T getX();
    /// Gets the vertical component of this vector
    T getY();
    /// Gets the component of this vector associated with the given axis
    default T get(Axis axis) {
        return axis == Axis.X ? getX() : getY();
    }
    /// Returns a new vector equivalent to this vector plus the other
    Immutable<T> addedTo(Vector<?> other);
    /// Returns a new vector equivalent to this vector plus the other
    Immutable<T> addedTo(T x, T y);
    /// Returns a new vector equivalent to this vector minus the other
    Immutable<T> subtractedBy(Vector<?> other);
    /// Returns a new vector equivalent to this vector minus the other
    Immutable<T> subtractedBy(T x, T y);

    /// Returns a new vector equivalent to this vector multiplied by the other
    default Immutable<T> multipliedBy(Vector<?> other) {
        return multipliedBy(other.getX().doubleValue(), other.getY().doubleValue());
    }

    /// Returns a new vector equivalent to this vector multiplied by the other
    Immutable<T> multipliedBy(double xFactor, double yFactor);

    /// Returns a new vector that is this vector with both components multiplied by the given factor
    default Immutable<T> scaledBy(double factor) {
        return multipliedBy(factor, factor);
    }

    /// Returns whether this vector is shorter than the given length
    default boolean isShorterThan(double len) {
        return lengthSqr() < len * len;
    }
    /// Returns the square of the length of this vector
    default double lengthSqr() {
        return getX().doubleValue() * getX().doubleValue() + getY().doubleValue() * getY().doubleValue();
    }
    /// Returns the length of this vector
    default double length() {
        return Math.sqrt(lengthSqr());
    }
    /// Returns the square of the distance between this vector and another
    default double distanceSqr(Vector<?> other) {
        return subtractedBy(other).lengthSqr();
    }
    /// Returns the distance between this vector and another
    default double distance(Vector<?> other) {
        return subtractedBy(other).length();
    }
    /// Returns a new vector which points in the same direction as this vector, but has a length of 1
    default ImmutableDouble normalized() {
        double length = length();
        if (length < 1E-5) return immutable(0D, 0D);
        return immutable(getX().doubleValue() / length, getY().doubleValue() / length);
    }

    /// Returns a new vector which points in the same direction as this vector, but has the given length
    default ImmutableDouble withLength(double length) {
        return normalized().scaledBy(length);
    }
    /// Returns a new vector that is this vector with the given axis set to the given value
    Immutable<T> with(Axis axis, T len);

    /// Returns whether this vector is essentially zero-length
    default boolean isZero() {
        return isShorterThan(1E-7);
    }

    /// Returns whether this vector is almost equal to another
    default boolean almostEquals(Vector<?> other) {
        return MathUtils.almostEquals(getX().doubleValue(), other.getX().doubleValue()) &&
                MathUtils.almostEquals(getY().doubleValue(), other.getY().doubleValue());
    }


    /// Returns a new immutable int vector
    static ImmutableInt immutable(int x, int y) {
        return new ImmutableInt(x, y);
    }
    /// Returns a new immutable double vector
    static ImmutableDouble immutable(double x, double y) {
        return new ImmutableDouble(x, y);
    }
    /// Returns a new mutable int vector
    static MutableInt mutable(int x, int y) {
        return new MutableInt(x, y);
    }
    /// Returns a new mutable double vector
    static MutableDouble mutable(double x, double y) {
        return new MutableDouble(x, y);
    }

    /// Returns a new immutable int vector
    static ImmutableInt immutableInt(Vector<?> vector) {
        return new ImmutableInt(vector);
    }
    /// Returns a new immutable double vector
    static ImmutableDouble immutableDouble(Vector<?> vector) {
        return new ImmutableDouble(vector);
    }
    /// Returns a new mutable int vector
    static MutableInt mutableInt(Vector<?> vector) {
        return new MutableInt(vector);
    }
    /// Returns a new mutable double vector
    static MutableDouble mutableDouble(Vector<?> vector) {
        return new MutableDouble(vector);
    }


    /// A vector with integer components
    interface Int extends Vector<Integer> {
        /// Gets the component of this vector associated with the given axis
        @Override
        default Integer get(Axis axis) {
            return Vector.super.get(axis);
        }

        /// Returns a new vector equivalent to this vector plus the other
        ImmutableInt addedTo(Integer x, Integer y);
        /// Returns a new vector equivalent to this vector minus the other
        ImmutableInt subtractedBy(Integer x, Integer y);

        /// Returns a new vector equivalent to this vector plus the other
        default ImmutableInt addedTo(Vector<?> other) {
            return addedTo(other.getX().intValue(), other.getY().intValue());
        }

        /// Returns a new vector equivalent to this vector minus the other
        default ImmutableInt subtractedBy(Vector<?> other) {
            return subtractedBy(other.getX().intValue(), other.getY().intValue());
        }

        /// Returns a new vector equivalent to this vector multiplied by the other
        @Override
        default ImmutableInt multipliedBy(Vector<?> other) {
            return multipliedBy(other.getX().doubleValue(), other.getY().doubleValue());
        }

        /// Returns a new vector equivalent to this vector multiplied by the other
        ImmutableInt multipliedBy(double xFactor, double yFactor);

        /// Returns a new vector that is this vector with both components multiplied by the given factor
        @Override
        default ImmutableInt scaledBy(double factor) {
            return multipliedBy(factor, factor);
        }

        /// Returns a new vector that is this vector with the given axis set to the given value
        @Override
        default ImmutableInt with(Axis axis, Integer len) {
            return immutable(axis == Axis.X ? len : getX(), axis == Axis.Y ? len : getY());
        }
    }
    /// A vector with double components
    interface Double extends Vector<java.lang.Double> {
        /// Gets the component of this vector associated with the given axis
        @Override
        default java.lang.Double get(Axis axis) {
            return Vector.super.get(axis);
        }

        /// Returns a new vector equivalent to this vector plus the other
        ImmutableDouble addedTo(java.lang.Double x, java.lang.Double y);
        /// Returns a new vector equivalent to this vector minus the other
        ImmutableDouble subtractedBy(java.lang.Double x, java.lang.Double y);

        /// Returns a new vector equivalent to this vector plus the other
        default ImmutableDouble addedTo(Vector<?> other) {
            return addedTo(other.getX().doubleValue(), other.getY().doubleValue());
        }

        /// Returns a new vector equivalent to this vector minus the other
        default ImmutableDouble subtractedBy(Vector<?> other) {
            return subtractedBy(other.getX().doubleValue(), other.getY().doubleValue());
        }

        /// Returns a new vector equivalent to this vector multiplied by the other
        @Override
        default ImmutableDouble multipliedBy(Vector<?> other) {
            return multipliedBy(other.getX().doubleValue(), other.getY().doubleValue());
        }

        /// Returns a new vector equivalent to this vector multiplied by the other
        ImmutableDouble multipliedBy(double xFactor, double yFactor);

        /// Returns a new vector that is this vector with both components multiplied by the given factor
        @Override
        default ImmutableDouble scaledBy(double factor) {
            return multipliedBy(factor, factor);
        }
        /// Returns a new vector that is this vector with the given axis set to the given value
        @Override
        default ImmutableDouble with(Axis axis, java.lang.Double len) {
            return immutable(axis == Axis.X ? len : getX(), axis == Axis.Y ? len : getY());
        }
    }
    /// Mutable vector
    interface Mutable<T extends Number> extends Vector<T> {
        /// Sets the x component of this vector
        Mutable<T> setX(T x);
        /// Sets the y component of this vector
        Mutable<T> setY(T y);
        /// Adds the given x and y components to this vector
        Mutable<T> add(T x, T y);
        /// Subtracts the given x and y components from this vector
        Mutable<T> subtract(T x, T y);
        /// Adds the given x and y components to this vector
        Mutable<T> add(Vector<?> other);
        /// Subtracts the given x and y components from this vector
        Mutable<T> subtract(Vector<?> other);
        /// Multiplies each component of this vector with the corresponding component of the other vector
        default Mutable<T> multiply(Vector<?> other) {
            return multiply(other.getX().doubleValue(), other.getY().doubleValue());
        }
        /// Multiplies each component of this vector with the corresponding given component
        Mutable<T> multiply(double xFactor, double yFactor);
        /// Multiplies each component of this vector by the given factor
        default Mutable<T> scale(double factor) {
            return multiply(factor, factor);
        }

        /// Sets the x and y components of this vector
        default Mutable<T> set(T x, T y) {
            setX(x);
            setY(y);
            return this;
        }
        /// Sets the x and y components of this vector
        default Mutable<T> set(Vector<T> other) {
            setX(other.getX());
            setY(other.getY());
            return this;
        }

        /// Makes the length of this vector 1
        Mutable<T> normalize();

        /// Sets the length of this vector to the given length
        default Mutable<T> setLength(double length) {
            return normalize().scale(length);
        }
    }
    /// Immutable vector
    interface Immutable<T extends Number> extends Vector<T> {

    }

    /// Vector of mutable integers
    class MutableInt implements Mutable<Integer>, Int {
        public int x;
        public int y;
        /// Creates a new mutable int
        public MutableInt(int x, int y) {
            this.x = x;
            this.y = y;
        }
        /// Creates a new mutable int
        public MutableInt(Vector<?> other) {
            this.x = other.getX().intValue();
            this.y = other.getY().intValue();
        }

        /// Gets the horizontal component of this vector
        @Override
        public Integer getX() {
            return x;
        }

        /// Gets the vertical component of this vector
        @Override
        public Integer getY() {
            return y;
        }

        /// Sets the x component of this vector
        @Override
        public MutableInt setX(Integer x) {
            this.x = x;
            return this;
        }

        /// Sets the y component of this vector
        @Override
        public MutableInt setY(Integer y) {
            this.y = y;
            return this;
        }

        /// Adds the given x and y components to this vector
        @Override
        public MutableInt add(Integer x, Integer y) {
            this.x += x;
            this.y += y;
            return this;
        }

        /// Subtracts the given x and y components from this vector
        @Override
        public MutableInt subtract(Integer x, Integer y) {
            this.x -= x;
            this.y -= y;
            return this;
        }

        /// Adds the given x and y components to this vector
        @Override
        public MutableInt add(Vector<?> other) {
            return add(other.getX().intValue(), other.getY().intValue());
        }

        /// Subtracts the given x and y components from this vector
        @Override
        public MutableInt subtract(Vector<?> other) {
            return subtract(other.getX().intValue(), other.getY().intValue());
        }

        /// Multiplies each component of this vector with the corresponding component of the other vector
        @Override
        public Mutable<Integer> multiply(Vector<?> other) {
            return Mutable.super.multiply(other);
        }

        /// Multiplies each component of this vector with the corresponding given component
        @Override
        public MutableInt multiply(double xFactor, double yFactor) {
            this.x *= (int) xFactor;
            this.y *= (int) yFactor;
            return this;
        }

        /// Multiplies each component of this vector by the given factor
        @Override
        public MutableInt scale(double factor) {
            return multiply(factor, factor);
        }

        /// Returns a new vector equivalent to this vector plus the other
        @Override
        public ImmutableInt addedTo(Integer x, Integer y) {
            return new ImmutableInt(this.x + x, this.y + y);
        }

        /// Returns a new vector equivalent to this vector minus the other
        @Override
        public ImmutableInt subtractedBy(Integer x, Integer y) {
            return new ImmutableInt(this.x - x, this.y - y);
        }

        /// Returns a new vector equivalent to this vector multiplied by the other
        @Override
        public ImmutableInt multipliedBy(double xFactor, double yFactor) {
            return new ImmutableInt((int) (x * xFactor), (int) (y * yFactor));
        }

        /// Makes the length of this vector 1
        @Override
        public MutableInt normalize() {
            double length = length();
            x = (int) (x / length);
            y = (int) (y / length);
            return this;
        }

        /// Sets the length of this vector to the given length
        @Override
        public MutableInt setLength(double length) {
            return normalize().scale(length);
        }

        /// Turns this vector into a string
        @Override
        public String toString() {
            return getClass().getSimpleName() + "{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        /// Returns whether this vector has the same value as another
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Int that)) return false;
            return x == that.getX() && y == that.getY();
        }

        /// Hashes this vector
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
    /// Vector of mutable doubles
    class MutableDouble implements Mutable<java.lang.Double>, Double {
        public double x;
        public double y;
        /// Creates a new mutable double
        public MutableDouble(double x, double y) {
            this.x = x;
            this.y = y;
        }
        /// Creates a new mutable double
        public MutableDouble(Vector<?> other) {
            this.x = other.getX().doubleValue();
            this.y = other.getY().doubleValue();
        }

        /// Gets the horizontal component of this vector
        @Override
        public java.lang.Double getX() {
            return x;
        }

        /// Gets the vertical component of this vector
        @Override
        public java.lang.Double getY() {
            return y;
        }

        /// Sets the x component of this vector
        @Override
        public MutableDouble setX(java.lang.Double x) {
            this.x = x;
            return this;
        }

        /// Sets the y component of this vector
        @Override
        public MutableDouble setY(java.lang.Double y) {
            this.y = y;
            return this;
        }

        /// Adds the given x and y components to this vector
        @Override
        public MutableDouble add(java.lang.Double x, java.lang.Double y) {
            this.x += x;
            this.y += y;
            return this;
        }

        /// Subtracts the given x and y components from this vector
        @Override
        public MutableDouble subtract(java.lang.Double x, java.lang.Double y) {
            this.x -= x;
            this.y -= y;
            return this;
        }

        /// Adds the given x and y components to this vector
        @Override
        public MutableDouble add(Vector<?> other) {
            return add(other.getX().doubleValue(), other.getY().doubleValue());
        }

        /// Subtracts the given x and y components from this vector
        @Override
        public MutableDouble subtract(Vector<?> other) {
            return subtract(other.getX().doubleValue(), other.getY().doubleValue());
        }

        /// Multiplies each component of this vector with the corresponding given component
        @Override
        public MutableDouble multiply(double xFactor, double yFactor) {
            this.x *= xFactor;
            this.y *= yFactor;
            return this;
        }

        /// Multiplies each component of this vector by the given factor
        @Override
        public MutableDouble scale(double factor) {
            return multiply(factor, factor);
        }

        /// Returns a new vector equivalent to this vector plus the other
        @Override
        public ImmutableDouble addedTo(java.lang.Double x, java.lang.Double y) {
            return new ImmutableDouble(this.x + x, this.y + y);
        }

        /// Returns a new vector equivalent to this vector minus the other
        @Override
        public ImmutableDouble subtractedBy(java.lang.Double x, java.lang.Double y) {
            return new ImmutableDouble(this.x - x, this.y - y);
        }

        /// Returns a new vector equivalent to this vector multiplied by the other
        @Override
        public ImmutableDouble multipliedBy(double xFactor, double yFactor) {
            return new ImmutableDouble(x * xFactor, y * yFactor);
        }

        /// Makes the length of this vector 1
        @Override
        public MutableDouble normalize() {
            double length = length();
            if (length < 1E-5) {
                x = y = 0;
            } else {
                x /= length;
                y /= length;
            }
            return this;
        }

        /// Sets the length of this vector to the given length
        @Override
        public MutableDouble setLength(double length) {
            return normalize().scale(length);
        }

        /// Turns this vector into a string
        @Override
        public String toString() {
            return getClass().getSimpleName() + "{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        /// Returns whether this vector has the same value as another
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Double that)) return false;
            return java.lang.Double.compare(x, that.getX()) == 0 && java.lang.Double.compare(y, that.getY()) == 0;
        }

        /// Hashes this vector
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
    /// Vector of immutable integers
    class ImmutableInt implements Immutable<Integer>, Int {
        public final int x;
        public final int y;
        /// Creates a new immutable int
        public ImmutableInt(int x, int y) {
            this.x = x;
            this.y = y;
        }
        /// Creates a new immutable int
        public ImmutableInt(Vector<?> other) {
            this.x = other.getX().intValue();
            this.y = other.getY().intValue();
        }

        /// Gets the horizontal component of this vector
        @Override
        public Integer getX() {
            return x;
        }

        /// Gets the vertical component of this vector
        @Override
        public Integer getY() {
            return y;
        }

        /// Returns a new vector equivalent to this vector plus the other
        @Override
        public ImmutableInt addedTo(Integer x, Integer y) {
            return new ImmutableInt(this.x + x, this.y + y);
        }

        /// Returns a new vector equivalent to this vector minus the other
        @Override
        public ImmutableInt subtractedBy(Integer x, Integer y) {
            return new ImmutableInt(this.x - x, this.y - y);
        }

        /// Returns a new vector equivalent to this vector multiplied by the other
        @Override
        public ImmutableInt multipliedBy(double xFactor, double yFactor) {
            return new ImmutableInt((int) (x * xFactor), (int) (y * yFactor));
        }

        /// Turns this vector into a string
        @Override
        public String toString() {
            return getClass().getSimpleName() + "{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        /// Returns whether this vector has the same value as another
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Int that)) return false;
            return x == that.getX() && y == that.getY();
        }

        /// Hashes this vector
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
    /// Vector of immutable doubles
    class ImmutableDouble implements Immutable<java.lang.Double>, Double {
        public final double x;
        public final double y;
        /// Creates a new immutable double
        public ImmutableDouble(double x, double y) {
            this.x = x;
            this.y = y;
        }
        /// Creates a new immutable double
        public ImmutableDouble(Vector<?> other) {
            this.x = other.getX().doubleValue();
            this.y = other.getY().doubleValue();
        }

        /// Gets the horizontal component of this vector
        @Override
        public java.lang.Double getX() {
            return x;
        }

        /// Gets the vertical component of this vector
        @Override
        public java.lang.Double getY() {
            return y;
        }

        /// Returns a new vector equivalent to this vector plus the other
        @Override
        public ImmutableDouble addedTo(java.lang.Double x, java.lang.Double y) {
            return new ImmutableDouble(this.x + x, this.y + y);
        }

        /// Returns a new vector equivalent to this vector minus the other
        @Override
        public ImmutableDouble subtractedBy(java.lang.Double x, java.lang.Double y) {
            return new ImmutableDouble(this.x - x, this.y -  y);
        }

        /// Returns a new vector equivalent to this vector multiplied by the other
        @Override
        public ImmutableDouble multipliedBy(double xFactor, double yFactor) {
            return new ImmutableDouble(x * xFactor, y * yFactor);
        }

        /// Turns this vector into a string
        @Override
        public String toString() {
            return getClass().getSimpleName() + "{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        /// Returns whether this vector has the same value as another
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Double that)) return false;
            return java.lang.Double.compare(x, that.getX()) == 0 && java.lang.Double.compare(y, that.getY()) == 0;
        }

        /// Hashes this vector
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
