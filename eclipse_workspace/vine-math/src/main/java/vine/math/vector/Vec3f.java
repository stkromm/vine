package vine.math.vector;

import vine.math.VineMath;

/**
 * Represents a immutable three-dimensional vector with single floating point
 * precision.
 *
 * @author Steffen Kromm, first created on 04.05.2016
 *
 */
public class Vec3f
{
    /**
     * Maximum difference two floating point values can differ and still count
     * as equal.
     */
    public static final float EPSILON = 0.000001f;
    /**
     * The x coordinate of the vector.
     */
    protected float           x;
    /**
     * The y coordinate of the vector.
     */
    protected float           y;
    /**
     * The z coordinate of the vector.
     */
    protected float           z;
    /**
     * Cached value so length is only calculated once (when the vector changes).
     * -1 means the length is invalid and must be calculated.
     */
    protected double          calculatedLength;

    /**
     * Signals that the calculated values (length of the vector) are out of date
     * have to be recalculated.
     */
    protected final void invalidate()
    {
        calculatedLength = -1;
    }

    /**
     * Creates a new Vector3f with the given x,y and z elements.
     *
     * @param x
     *            The x element of the new vector
     * @param y
     *            The y element of the new vector
     * @param z
     *            The z element of the new vector
     */
    public Vec3f(final float x, final float y, final float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        invalidate();
    }

    public Vec3f(final Vec3f vector)
    {
        x = vector.x;
        y = vector.y;
        z = vector.z;
        invalidate();
    }

    /**
     * Getter.
     *
     * @return The x element value
     */
    public float getX()
    {
        return x;
    }

    /**
     * Getter.
     *
     * @return y element value
     */
    public float getY()
    {
        return y;
    }

    /**
     * Getter.
     *
     * @return The z element value
     */
    public float getZ()
    {
        return z;
    }

    /**
     * Returns the dot product of this Vector2f and the given. Callers guarantee
     * that the given vector is a valid object.
     *
     * @param vector
     *            The vector used to calculate a dot product with this vector
     * @return The dot product of this and the given vector
     */
    public float dot(final Vec3f vector)
    {
        return vector == null ? 0 : vector.x * x + vector.y * y + z * vector.z;
    }

    /**
     * Multiplies the elements of this vector with the given scale float value.
     *
     * @param factor
     *            The factor that is multiplied with the elements of this
     *            vector.
     */
    public void scale(final double factor)
    {
        x *= factor;
        y *= factor;
        z *= factor;
        calculatedLength *= factor;
    }

    /**
     * Returns the length of this Vector2f.
     *
     * @return The length of this vector
     */
    public double length()
    {
        if (calculatedLength == -1)
        {
            calculatedLength = VineMath.sqrt(dot(this));
        }
        return calculatedLength;
    }

    /**
     * Returns the squared length of this Vector2f.
     *
     * @return The squared length of this vector
     */
    public float squaredLength()
    {
        return dot(this);
    }

    /**
     * Calculates the inner angle between this and the given vector.
     *
     * @param vector
     *            The vector, which angle between this vector is calculated
     * @return The angle between this and the given vector.
     */
    public double getAngle(final Vec3f vector)
    {
        if (vector == null)
        {
            return 0;
        }
        final double length = length();
        if (length <= EPSILON)
        {
            return 0;
        }
        final double vectorLength = vector.length();
        if (vectorLength <= EPSILON)
        {
            return 0;
        }
        return this.dot(vector) / (length * vectorLength);
    }

    /**
     * Calculates the cross product of this and the given vector.
     *
     * @param vector
     *            The vector which is cross multiplied with this vector
     * @return The cross product of this and the given vector, which is a
     *         Vector3f object.
     */
    public Vec3f cross(final Vec3f vector)
    {
        if (vector == null)
        {
            return new Vec3f(0, 0, 0);
        }
        return new Vec3f(y * vector.z - z * vector.y, z * vector.x - x * vector.z, x * vector.y - y * vector.x);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (!(object instanceof Vec3f))
        {
            return false;
        }
        final Vec3f vector = (Vec3f) object;
        final float diff = vector.x - x + vector.y - y + vector.z - z;
        return VineMath.abs(diff) <= 3 * EPSILON;
    }

    @Override
    public int hashCode()
    {
        int result = 1;
        result = 31 * result + Float.floatToIntBits(x);
        result = 11 * result + Float.floatToIntBits(y);
        result = 7 * result + Float.floatToIntBits(z);
        return result;
    }

    @Override
    public String toString()
    {
        return "Vector3f(" + x + "," + y + "," + z + ")";
    }
}
