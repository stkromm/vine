package vine.math.vector;

import static vine.math.VineMath.abs;
import static vine.math.VineMath.sqrt;
import static vine.math.vector.Vec2Util.VEC2_EPSILON;

import java.io.Serializable;

/**
 * Represents a mathematical immutable vector 2d.
 *
 * Thread-safe.
 *
 * @author Steffen
 *
 */
public class Vec2f implements Serializable
{
    private static final long serialVersionUID = -48013626869712862L;
    /**
     * Vector that represents the x axis;
     */
    public static final Vec2f X_AXIS           = new Vec2f(1, 0);
    /**
     * Vector that represents the y axis;
     */
    public static final Vec2f Y_AXIS           = new Vec2f(0, 1);
    /**
     * Vector that represents the x axis in negative direction;
     */
    public static final Vec2f NEGATIV_X_AXIS   = new Vec2f(-1, 0);
    /**
     * Vector that represents the y axis in negative direction;
     */
    public static final Vec2f NEGATIVE_Y_AXIS  = new Vec2f(0, -1);
    /**
     * Zero vector.
     */
    public static final Vec2f ZERO             = new Vec2f(0, 0);
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
        // Empty constructor if you don't need to initialize the vector by
        // construction.
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
     *
     * @param vector
     *            the vector added to this vector.
     */
    public Vec2f(final Vec2f vector)
    {
        x = vector.x;
        y = vector.y;
    }

    /**
     * Getter.
     *
     * @return the float value of the x element.
     */
    public final float getX()
    {
        return x;
    }

    /**
     * Getter.
     *
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
    public final float dot(final Vec2f vector)
    {
        return vector == null ? 0 : Vec2Util.dot(x, y, vector.getX(), vector.getY());
    }

    /**
     * Calculates the length of this Vector2f.
     *
     * @return the length of this Vector2f.
     */
    public final double length()
    {
        return Vec2Util.length(x, y);
    }

    /**
     * Calculates the squared length of this Vector2f.
     *
     * @return the squared length of this Vector2f.
     */
    public final float squaredLength()
    {
        return Vec2Util.squaredLength(x, y);
    }

    /**
     * Returns the squared distance from the point defined by this Vec2f to the
     * point defined by the given Vec2f.
     *
     * @param vector
     *            Point to which the squared distance is calculated
     * @return The squared distance to the given point
     */
    public final float squaredDistance(final Vec2f vector)
    {
        return Vec2Util.squaredLength(vector.getX() - x, vector.getY() - y);
    }

    /**
     * Returns the distance from the point defined by this Vec2f to the point
     * defined by the given Vec2f.
     *
     * @param vector
     *            Point to which the distance is calculated
     * @return The distance to the given point
     */
    public final double distance(final Vec2f vector)
    {
        return sqrt(squaredDistance(vector));
    }

    /**
     * Calculates the inner angle between this and the given vector.
     *
     * @param vector
     *            The vector that angle between this vector is calculated
     * @return The angle between this and the given vector
     */
    public final double getAngle(final Vec2f vector)
    {
        if (vector == null)
        {
            return 0;
        }
        return Vec2Util.getAngle(x, y, vector.x, vector.y);
    }

    /**
     * @return True, if the vector is normalized, that is, length == 1
     */
    public final boolean isNormalized()
    {
        return abs(squaredLength() - 1) < VEC2_EPSILON;
    }

    /**
     * @param vector
     *            The vector that is checked for equality.
     * @return True, if the vector is numerical equal.
     *
     * @see #equalWithEpsilon(float, float)
     */
    public final boolean equalWithEpsilon(final Vec2f vector)
    {
        if (vector == null)
        {
            return false;
        }
        return equalWithEpsilon(vector.getX(), vector.getY());
    }

    /**
     * Returns true, if the given vector is numerical equal to this vector (with
     * error tolerance).
     *
     * @param x
     *            x Coordinate of the compared vector.
     * @param y
     *            y Coordinate of the compared vector.
     * @return True, if the vectors are numerical equal.
     */
    public final boolean equalWithEpsilon(final float x, final float y)
    {
        final float xDif = x - this.x;
        final float yDif = y - this.y;
        return abs(xDif) + abs(yDif) <= 2 * VEC2_EPSILON;
    }

    @Override
    public final boolean equals(final Object object)
    {
        if (!(object instanceof Vec2f))
        {
            return false;
        }
        final Vec2f vector = (Vec2f) object;
        return vector.x == x && vector.y == y;
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
        return "Vector2f(" + x + "," + y + ")";
    }
}