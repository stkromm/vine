package vine.math;

/**
 * Represents a mathematical vector 2d and performs transformations on this
 * object with its methods.
 * 
 * @author Steffen
 *
 */
public class Vector2f
{
    /**
     * Maximum difference two floating point values can differ and still count
     * as equal.
     */
    protected static final float EPSILON = 0.000001f;
    private float                x;
    private float                y;

    /**
     * Creates a new Vector2f object, that represents the mathematical vector 2d
     * with the given float element.
     * 
     * @param x
     *            The x value of the vector
     * @param y
     *            The y value of the vector
     */
    public Vector2f(final float x, final float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the float value of the x element.
     */
    public final float getX()
    {
        return x;
    }

    /**
     * @param x
     *            x value of the vector.
     */
    public final void setX(float x)
    {
        this.x = x;
    }

    /**
     * @return the float value of the y element.
     */
    public final float getY()
    {
        return y;
    }

    /**
     * @param y
     *            y value of the vector.
     */
    public final void setY(float y)
    {
        this.y = y;
    }

    /**
     * Returns a perpendicular Vector2f for this vector. Returns the 0-Vector if
     * there is no perpendicular vector (simply because this vector has length
     * zero).
     * 
     * @return The new created perpendicular vector.
     */
    public final Vector2f getPerpendicular()
    {
        return new Vector2f(-y, x);
    }

    /**
     * Adds the given values to the corresponding elements of this Vector2f.
     * 
     * @param x
     *            The x value added to this x value
     * @param y
     *            The y value added to this y value
     */
    public final void add(final float x, final float y)
    {
        this.x += x;
        this.y += y;
    }

    /**
     * Adds the elements of the given vector the elements of this Vector2f.
     * 
     * @param vector
     *            The vector, which elements are added to the elements of this
     *            vector
     */
    public final void add(final Vector2f vector)
    {
        if (vector != null)
        {
            add(vector.getX(), vector.getY());
        }
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
    public final strictfp float dot(final Vector2f vector)
    {

        return vector == null ? 0 : x * vector.getX() + y * vector.getY();
    }

    /**
     * @param factor
     *            factor, that is multiplied element wise with the vector.
     */
    public final strictfp void scale(final double factor)
    {
        x *= factor;
        y *= factor;
    }

    /**
     * Calculates the length of this Vector2f.
     * 
     * @return the length of this Vector2f
     */
    public final strictfp double length()
    {
        return Math.sqrt(dot(this));
    }

    /**
     * Calculates the inner angle between this and the given vector.
     * 
     * @param vector
     *            The vector that angle between this vector is calculated
     * @return The angle between this and the given vector
     */
    public final strictfp double getAngle(final Vector2f vector)
    {
        if (vector == null)
        {
            return 0;
        }
        final double thisLength = length();
        if (thisLength <= EPSILON)
        {
            return 0;
        }
        final double vectorLength = vector.length();
        if (vectorLength <= EPSILON)
        {
            return 0;
        }
        return this.dot(vector) / (thisLength * vectorLength);
    }

    /**
     * Normalizes this vector.
     */
    public final void normalize()
    {
        if (Math.abs(x) + Math.abs(y) <= 2 * EPSILON)
        {
            return;
        }
        final double inversedLength = 1 / length();
        scale(inversedLength);
    }

    @Override
    public final boolean equals(Object object)
    {
        if (object == null)
        {
            return false;
        }
        if (!(object instanceof Vector2f))
        {
            return false;
        }
        final Vector2f vector = (Vector2f) object;
        return Math.abs(vector.getX() - x + vector.getY() - y) <= 2 * EPSILON;
    }
}
