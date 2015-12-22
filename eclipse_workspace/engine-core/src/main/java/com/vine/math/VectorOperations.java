package com.vine.math;

public interface VectorOperations<V> {
    /**
     * Returns true, if each element of the given Vector2f is equivalent to the
     * corresponding element of this Vector2f.
     */
    boolean equalTo(final V vector);

    /**
     * Adds the elements of the given vector the elements of this Vector2f.
     */
    void add(final V vector);

    /**
     * Returns the dot product of this Vector2f and the given. Callers guarantee
     * that the given vector is a valid object.
     */
    float dot(final V vector);

    /**
     * Multiplies the elements of this vector with the given scale float value.
     */
    void scale(final float factor);

    /**
     * Returns the length of this Vector2f.
     */
    double length();

    /**
     * Calculates the inner angle between this and the given vector.
     */
    double getAngle(final V vector);

    /**
     * Normalizes this vector.
     */
    void normalize();
}
