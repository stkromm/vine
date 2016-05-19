package vine.math.vector;

import vine.math.VineMath;

public class MutableVec3f extends Vec3f
{

    public MutableVec3f(final float x, final float y, final float z)
    {
        super(x, y, z);
    }

    /**
     * @param z
     *            The new z element value
     */
    public void setZ(final float z)
    {
        this.z = z;
        invalidate();
    }

    /**
     * @param y
     *            The new y element value
     */
    public void setY(final float y)
    {
        this.y = y;
        invalidate();
    }

    /**
     * @param x
     *            The new x element value
     */
    public void setX(final float x)
    {
        this.x = x;
        invalidate();
    }

    /**
     * Adds the given values to the corresponding elements of this Vector3f.
     *
     * @param x
     *            The addition to the x element
     * @param y
     *            The addition to the y element
     * @param z
     *            The addition to the z element
     */
    public void add(final float x, final float y, final float z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        invalidate();
    }

    /**
     * Adds the elements of the given vector the elements of this Vector3f.
     *
     * @param vector
     *            The vector, which elements are added to the corresponding
     *            elements of this vector.
     */
    public void add(final Vec3f vector)
    {
        if (vector == null)
        {
            return;
        }
        add(vector.getX(), vector.getY(), vector.getZ());
        invalidate();
    }

    /**
     * Normalizes this vector.
     */
    public void normalize()
    {
        final float length = dot(this);
        if (length <= EPSILON)
        {
            return;
        }
        final float inversedLength = (float) (1 / VineMath.sqrt(dot(this)));
        scale(inversedLength);
        calculatedLength = 1;
    }

    /**
     * Updates the values of this Vec3f to be the cross product of this vector
     * and the given.
     */
    public void crossInThis(final Vec3f vector)
    {
        final float newX = y * vector.z - z * vector.y;
        final float newY = z * vector.x - x * vector.z;
        final float newZ = x * vector.y - y * vector.x;
        x = newX;
        y = newY;
        z = newZ;
        invalidate();
    }
}
