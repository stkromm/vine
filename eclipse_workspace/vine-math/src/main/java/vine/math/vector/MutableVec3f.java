package vine.math.vector;

public class MutableVec3f extends Vec3f
{

    public MutableVec3f(float x, float y, float z)
    {
        super(x, y, z);
    }

    /**
     * @param z
     *            The new z element value
     */
    public void setZ(float z)
    {
        this.z = z;
        invalidate();
    }

    /**
     * @param y
     *            The new y element value
     */
    public void setY(float y)
    {
        this.y = y;
        invalidate();
    }

    /**
     * @param x
     *            The new x element value
     */
    public void setX(float x)
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
    public void add(float x, float y, float z)
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
    public void add(Vec3f vector)
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
        final float inversedLength = (float) (1 / Math.sqrt(dot(this)));
        scale(inversedLength);
        this.length = 1;
    }
}
