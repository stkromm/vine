package vine.math;

public class MutableVec2f extends Vec2f
{

    /**
     * 
     */
    private static final long serialVersionUID = 7194729065343295239L;

    /**
     * Creates a zero vector.
     */
    public MutableVec2f()
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
    public MutableVec2f(final float x, final float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a copy of the given vector.
     */
    public MutableVec2f(Vec2f vector)
    {
        this.set(vector);
    }

    /**
     * @param x
     *            x value of the vector.
     */
    public void setX(float x)
    {
        this.x = x;
    }

    /**
     * @param y
     *            y value of the vector.
     */
    public void setY(float y)
    {
        this.y = y;
    }

    /**
     * Sets the x and y value of this vector equal to the given.
     */
    public final void set(Vec2f vec)
    {
        x = vec.x;
        y = vec.y;
    }

    public final void set(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds the given values to the corresponding elements of this Vector2f.
     * 
     * @param x
     *            The x value added to this x value
     * @param y
     *            The y value added to this y value
     */
    public void add(final float x, final float y)
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
    public void add(final Vec2f vector)
    {
        if (vector != null)
        {
            add(vector.getX(), vector.getY());
        }
    }

    /**
     * Substracts the elements of the given vector from the elements of this
     * Vector2f.
     */
    public void sub(final Vec2f vector)
    {
        if (vector != null)
        {
            add(-vector.getX(), -vector.getY());
        }
    }

    /**
     * @param factor
     *            factor, that is multiplied element wise with the vector.
     */
    @Override
    public strictfp void scale(final double factor)
    {
        x *= factor;
        y *= factor;
    }

    public strictfp void scale(final double xFactor, final double yFactor)
    {
        x *= xFactor;
        y *= yFactor;
    }

    public strictfp void rotateDegrees(final float degrees)
    {
        rotateRadians((float) Math.toRadians(degrees));
    }

    public strictfp void rotateRadians(final float radians)
    {
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);
        this.x = this.x * cos - this.y * sin;
        this.y = this.x * sin + this.y * cos;
    }

    public void rotate180()
    {
        this.scale(-1);
    }

    public void rotate90(boolean clockwise)
    {
        float tmpX = x;
        if (clockwise)
        {
            x = -y;
            y = tmpX;
        } else
        {
            x = y;
            y = -tmpX;
        }
    }

    /**
     * Normalizes this vector.
     */
    @Override
    public void normalize()
    {
        if (Math.abs(x) + Math.abs(y) <= 2 * EPSILON)
        {
            return;
        }
        final double inversedLength = 1 / length();
        scale(inversedLength);
    }
}
