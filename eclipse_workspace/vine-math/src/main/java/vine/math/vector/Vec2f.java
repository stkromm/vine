package vine.math.vector;

import java.io.Serializable;

/**
 * Represents a mathematical vector 2d and performs transformations on this
 * object with its methods.
 *
 * @author Steffen
 *
 */
public class Vec2f implements Serializable
{
    private static final long serialVersionUID = -48013626869712862L;
    public static final Vec2f X_AXIS           = new Vec2f(1, 0);
    /**
     * Maximum difference two floating point values can have and still count as
     * equal.
     */
    public static final float EPSILON          = 0.000001f;
    /**
     * x Value of the vector.
     */
    protected float           x;
    /**
     * y Value of the vector.
     */
    protected float           y;

    /**
     * Creates a zero vector.
     */
    public Vec2f()
    {

    }

    /**
     * Creates a new Vector2f object, that represents the mathematical vector 2d
     * with the given float element.
     *
     * @param x
     *            The x value of the vector
     * @param y
     *            The y value of the vector
     */
    public Vec2f(final float x, final float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a copy of the given vector.
     */
    public Vec2f(final Vec2f vector)
    {
        x = vector.x;
        x = vector.y;
    }

    /**
     * @return the float value of the x element.
     */
    public final float getX()
    {
        return x;
    }

    /**
     * @return the float value of the y element.
     */
    public final float getY()
    {
        return y;
    }

    /**
     * Returns a perpendicular Vector2f for this vector. Returns the 0-Vector if
     * there is no perpendicular vector (simply because this vector has length
     * zero).
     *
     * @return The new created perpendicular vector.
     */
    public final Vec2f getPerpendicular()
    {
        return new Vec2f(-y, x);
    }

    /**
     * Calculates the dot product with 2 vectors.
     *
     * @param vector
     *            The vector, that is used to calculate a dot product with this
     *            vector.
     *
     * @return The dot product of this vector and the given.
     *
     */
    public final strictfp float dot(final Vec2f vector)
    {

        return vector == null ? 0 : dot(x, vector.getX(), y, vector.getY());
    }

    /**
     * Calculates the dot product of the given 2 elementwise given vectors.
     */
    public static final strictfp float dot(final float x1, final float y1, final float x2, final float y2)
    {
        return x2 * x1 + y2 * y1;
    }

    /**
     * Calculates the length of this Vector2f.
     *
     * @return the length of this Vector2f
     */
    public final strictfp double length()
    {
        return length(x, y);
    }

    /**
     * @return The length of the given elementwise vector.
     */
    public static final strictfp double length(final float x, final float y)
    {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Calculates the inner angle between this and the given vector.
     *
     * @param vector
     *            The vector that angle between this vector is calculated
     * @return The angle between this and the given vector
     */
    public final strictfp double getAngle(final Vec2f vector)
    {
        if (vector == null)
        {
            return 0;
        }
        return getAngle(x, y, vector.x, vector.y);
    }

    public static final strictfp double getAngle(final float x1, final float y1, final float x2, final float y2)
    {
        final double length1 = length(x1, y1);
        if (length1 <= EPSILON)
        {
            return 0;
        }
        final double length2 = length(x2, y2);
        if (length2 <= EPSILON)
        {
            return 0;
        }
        return dot(x1, x2, y1, y2) / (length1 * length2);
    }

    /**
     * @param factor
     *            factor, that is multiplied element wise with the vector.
     */
    protected strictfp void scale(final double factor)
    {
        x *= factor;
        y *= factor;
    }

    /**
     * Normalizes this vector.
     */
    protected void normalize()
    {
        if (Math.abs(x) + Math.abs(y) <= 2 * EPSILON)
        {
            return;
        }
        final double inversedLength = 1 / length();
        scale(inversedLength);
    }

    public final boolean isNormalized()
    {
        return this.length() - 1 < EPSILON;
    }

    public final boolean equal(final Vec2f vector)
    {
        if (vector == null)
        {
            return false;
        }
        return equal(vector.getX(), vector.getY());
    }

    public final boolean equal(final float x, final float y)
    {
        return Math.abs(x - this.x + y - this.y) <= 2 * EPSILON;
    }

    @Override
    public final boolean equals(final Object object)
    {
        if (!(object instanceof Vec2f))
        {
            return false;
        }
        final Vec2f vector = (Vec2f) object;
        return equal(vector);
    }

    @Override
    public int hashCode()
    {
        int result = 1;
        result = 31 * result + Float.floatToIntBits(x);
        result = 7 * result + Float.floatToIntBits(y);
        return result;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "(" + x + "," + y + ")";
    }
}
