package com.vine.math;

public class Vector2f implements VectorOperations<Vector2f> {
    /**
     * Maximum difference two floating point values can differ and still count
     * as equal.
     */
    protected static final float EPSILON = 0.000001f;
    private float x;
    private float y;

    public Vector2f(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public final float getX() {
        return x;
    }

    public final void setX(float x) {
        this.x = x;
    }

    public final float getY() {
        return y;
    }

    public final void setY(float y) {
        this.y = y;
    }

    /**
     * Returns a perpendicular Vector2f for this vector. Returns the 0-Vector if
     * there is no perpendicular vector (simply because this vector has length
     * zero).
     */
    public final Vector2f getPerpendicular() {
        return new Vector2f(-y, x);
    }

    @Override
    public final boolean equalTo(final Vector2f vector) {
        return vector != null ? Math.abs(vector.getX() - x + vector.getY() - y) <= 2 * EPSILON : false;
    }

    /**
     * Adds the given values to the corresponding elements of this Vector2f.
     */
    public final void add(final float x, final float y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public final void add(final Vector2f vector) {
        if (vector != null) {
            add(vector.getX(), vector.getY());
        }
    }

    @Override
    public final strictfp float dot(final Vector2f vector) {
        return vector == null ? 0 : x * vector.getX() + y * vector.getY();
    }

    @Override
    public final void scale(final float factor) {
        x *= factor;
        y *= factor;
    }

    @Override
    public final double length() {
        return Math.sqrt(dot(this));
    }

    @Override
    public final strictfp double getAngle(final Vector2f vector) {
        if (vector == null) {
            return 0;
        }
        final double length = length();
        if (length <= EPSILON) {
            return 0;
        }
        final double vectorLength = vector.length();
        if (vectorLength <= EPSILON) {
            return 0;
        }
        return this.dot(vector) / (length * vectorLength);
    }

    @Override
    public final void normalize() {
        if (Math.abs(x) + Math.abs(y) <= 2 * EPSILON) {
            return;
        }
        final float inversedLength = 1 / (float) length();
        scale(inversedLength);
    }
}