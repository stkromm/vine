package vine.math;

/**
 * @author Steffen
 *
 */
public class Vec3f
{
    /**
     * Maximum difference two floating point values can differ and still count
     * as equal.
     */
    protected static final float EPSILON = 0.000001f;
    protected float              x;
    protected float              y;
    protected float              z;

    protected double             length;

    protected void invalidate()
    {
        length = -1;
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
    public Vec3f(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        invalidate();
    }

    /**
     * @return The x element value
     */
    public float getX()
    {
        return x;
    }

    /**
     * @return y element value
     */
    public float getY()
    {
        return y;
    }

    /**
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
    public strictfp float dot(Vec3f vector)
    {
        return vector == null ? 0 : vector.getX() * x + vector.getY() * y + z * vector.getZ();
    }

    /**
     * Multiplies the elements of this vector with the given scale float value.
     * 
     * @param factor
     *            The factor that is multiplied with the elements of this
     *            vector.
     */
    public void scale(double factor)
    {
        x *= factor;
        y *= factor;
        z *= factor;
        length *= factor;
    }

    /**
     * Returns the length of this Vector2f.
     * 
     * @return The length of this vector
     */
    public double length()
    {
        if (length == -1)
        {
            length = Math.sqrt(dot(this));
        }
        return length;
    }

    /**
     * Calculates the inner angle between this and the given vector.
     * 
     * @param vector
     *            The vector, which angle between this vector is calculated
     * @return The angle between this and the given vector.
     */
    public double getAngle(Vec3f vector)
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
    public Vec3f cross(Vec3f vector)
    {
        if (vector == null)
        {
            return new Vec3f(0, 0, 0);
        }
        return new Vec3f(y * vector.getZ() - z * vector.getY(), z * vector.getX() - x * vector.getZ(),
                x * vector.getY() - y * vector.getX());
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object == null)
        {
            return false;
        }
        if (!(object instanceof Vec3f))
        {
            return false;
        }
        final Vec3f vector = (Vec3f) object;
        return Math.abs(vector.getX() - x + vector.getY() - y + vector.getZ() - z) <= 3 * EPSILON;
    }

}
